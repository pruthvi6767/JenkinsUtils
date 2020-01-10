package main.groovy

import com.cloudbees.plugins.credentials.impl.*;
import com.cloudbees.plugins.credentials.*;
import com.cloudbees.plugins.credentials.domains.*;
import org.jenkinsci.plugins.plaincredentials.impl.*;
import org.jenkinsci.plugins.plaincredentials.*;
import org.jenkinsci.plugins.plaincredentials.StringCredentials
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl
import hudson.util.Secret
i
import java.util.logging.Logger

Logger logger = Logger.getLogger("")

def env = System.getenv()
Credentials credentials = null
globalCredentialsStore = SystemCredentialsProvider.getInstance().getStore()
credentials = new StringCredentialsImpl(CredentialsScope.GLOBAL,"secret_git_token","Github Token for robot",new Secret(env['ROBOT_TOKEN']))
globalCredentialsStore.addCredentials(Domain.global(), credentials)
logger.info("added .... Token..")








