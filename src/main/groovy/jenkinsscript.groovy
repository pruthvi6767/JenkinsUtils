
import jenkins.model.Jenkins
import hudson.model.*
import groovy.transform.Field
import java.util.logging.Logger

//global
@Field logger = Logger.getLogger("")
@Field jenkins = Jenkins.getInstanceOrNull()
@Field ENV = System.getenv()
@Field COUNT = 1
@Field RETRIES = ENV['HEALTH_RETRIES']?: 5
@Field SLEEP_COUNTER = 0
@Field TIME_OUT = ENV['STARTUP_TRIGGER_TIME_OUT']?: 120  // 120 * 5000ms = 10 mins

def runStartUpTrigger() {
    logger.info("Starting Job Triggers during init..")
    if(checkStartUpHealth()){
        TriggerJobs()
    }

}

class StartUpCause extends Cause {
    @Override
    public String getShortDescription() {
        return 'StartedBy: Jenkins-Master Deploy'
    }
}
// return object
def getDelayStart(){
    COUNT++
    return COUNT*5
}
//returns long-wait queue
def isBlockedQueue(time) {
    return  time > TIME_OUT
}


def TriggerJobs() {
    projects = jenkins.getAllItems()
    queue = jenkins.getQueue()
    while (projects) {
        if (queue.isEmpty()) {
            logger.fine("Enqueuing Jobs...")
            SLEEP_COUNTER = 0
            projects.pop().scheduleBuild(getDelayStart(), new StartUpCause())
        }
        if(isBlockedQueue(SLEEP_COUNTER)) {
            SLEEP_COUNTER = 0
            log.fine("Clearing Blocked Queue...")
            queue.clear()
        }
        SLEEP_COUNTER++
        sleep(5000)
    }
}

def checkStartUpHealth(){
    //jenkins is healthy
    while(RETRIES > 0){
        logger.info("Check Jenkins Startup Health")
        if(jenkins.getInstallState().isSetupComplete())
            logger.fine("Jenkins is Healthy")
            return true
        RETRIES--
        sleep(5000)
    }
    logger.fine("Jenkins is Unhealthy..")
    return false
}
runStartUpTrigger()

