//========================================================================
//$Id: IJettyService.java 474 2012-01-23 03:07:14Z janb.webtide $
//Copyright 2008 Mort Bay Consulting Pty. Ltd.
//------------------------------------------------------------------------
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//http://www.apache.org/licenses/LICENSE-2.0
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//========================================================================

package com.fantong.android.jetty.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.eclipse.jetty.deploy.DeploymentManager;
import org.eclipse.jetty.deploy.providers.WebAppProvider;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.TagLibConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;


import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * IJettyService
 * <p>
 * Android Service which runs the Jetty server, maintaining it in the active
 * Notifications so that the user can return to the IJetty Activity to control
 * it at any time.
 */
public class JettyService extends Service {

    private static final String TAG = "Jetty";

    public static final String[] __configurationClasses = new String[]{
            AndroidWebInfConfiguration.class.getName(), WebXmlConfiguration.class.getName(), JettyWebXmlConfiguration.class.getName(),
            TagLibConfiguration.class.getName(), FragmentConfiguration.class.getName(), MetaInfConfiguration.class.getName()};



    // private NotificationManager mNM;
    private Server server;


    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public IBinder onBind(Intent intent) {
        return new JettyBinder();
    }



    public class JettyBinder extends Binder {

        private int port;
        private String rootPath;
        private String defaultsDescriptorPath;
        private String tempDirPath;

        public void startServer() throws Exception {
            server = new Server(port);
            ContextHandlerCollection handlers = new ContextHandlerCollection();
            WebAppProvider appProvider = new WebAppProvider();
            appProvider.setMonitoredDirName(rootPath);
            DeploymentManager deploymentManager = new DeploymentManager();
            server.setDumpBeforeStop(true);
            deploymentManager.setContexts(handlers);
            appProvider.setDeploymentManager(deploymentManager);
            appProvider.setDefaultsDescriptor(defaultsDescriptorPath);
            appProvider.setConfigurationClasses(__configurationClasses);
            appProvider.setTempDir(new File(tempDirPath));
            server.addBean(deploymentManager);
            server.setHandler(handlers);
            server.addBean(appProvider);
            server.start();
        }

        public void setPort(int port) {
            this.port = port;
        }

        public void setRootPath(String rootPath) {
            this.rootPath = rootPath;
        }

        public void setDefaultsDescriptorPath(String defaultsDescriptorPath) {
            this.defaultsDescriptorPath = defaultsDescriptorPath;
        }

        public void setTempDirPath(String tempDirPath) {
            this.tempDirPath = tempDirPath;
        }

        public void stopServer() throws Exception {
            Log.i(TAG, "Jetty stopping");
            if (server != null) {
                server.stop();
            }
            Log.i(TAG, "Jetty server stopped");
        }
    }

}

