/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpango.ussd;

import com.google.gson.Gson;
import com.mpango.ussd.MoUssdReq;
import com.mpango.ussd.MoUssdResp;
import java.io.*;
import java.lang.reflect.Constructor;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author jmulutu
 */
//@WebServlet("/UssdHandler")
public class MoUssdReceiver extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(MoUssdReceiver.class.getName());
    private List<MoUssdListener> moListenerList = new ArrayList();
    private ExecutorService executorService;
    private static int sdpMoReceiverThreadCount;
    //private ServletContext ctx;

    public void init(ServletConfig config) throws ServletException {
        // initialise the web.xml config
        super.init(config);

        // get the servlet context
        //ctx = getServletContext();        
        // load all on-startup configs
        //String TESTMENU = (String)ctx.getAttribute("TESTMENU"); 
        //System.out.println("----> TESTMENU MOUSSDRECEIVER <---- " + TESTMENU ) ;
        String receiverClassName = config.getInitParameter("ussdReceiver");
        initializeListeners(receiverClassName);
        initializeReceivingThreadPool();
    }

    private void initializeReceivingThreadPool() {
        this.executorService = Executors.newCachedThreadPool(new ThreadFactory() {
            public Thread newThread(Runnable r) {
                return new Thread(r, "sdp-mo-receiver-thread-" + MoUssdReceiver.class);
            }
        });
    }

    private void initializeListeners(String receiverClassName) {
        try {
            if (receiverClassName != null) {
                Class listener = Class.forName(receiverClassName);
                Constructor constructor = listener.getConstructor(new Class[0]);
                Object object = constructor.newInstance(new Object[0]);
                if ((object instanceof MoUssdListener)) {
                    MoUssdListener moUssdListener = (MoUssdListener) object;
                    moUssdListener.init();
                    this.moListenerList.add(moUssdListener);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Exception occurred while initializing listener", e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contentType = req.getContentType();

        if ((contentType == null) || (!contentType.equals("application/x-www-form-urlencoded"))) {
            resp.setStatus(415);
            resp.getWriter().println("Only application/json is supporting");
            return;
        }
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        Gson gson = new Gson();
        try {
            String readContent = readStringContent(req);

            System.out.println("readContent::: " + readContent);

            MoUssdReq moUssdReq = (MoUssdReq) gson.fromJson(readContent, MoUssdReq.class);
            for (MoUssdListener moUssdListener : this.moListenerList) {
                MoUssdResp replyyyy = fireMoEvent(moUssdListener, moUssdReq);
                try {
                    resp.getWriter().print(gson.toJson(replyyyy));
                } catch (IOException ex) {
                    Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            /*MoUssdResp moUssdResp = new MoUssdResp();
             moUssdResp.setStatusCode("S1000");
             moUssdResp.setStatusDetail("Success");
             moUssdResp.setPAGE_STRING("test message");
             resp.getWriter().print(gson.toJson(moUssdResp));*/
        } catch (Exception e) {
            MoUssdResp moUssdResp = new MoUssdResp();
            moUssdResp.setStatusCode("E1601");
            moUssdResp.setStatusDetail("System error occurred");
            try {
                resp.getWriter().print(gson.toJson(moUssdResp));
            } catch (IOException e2) {
                LOGGER.log(Level.INFO, "Unexpected error occurred", e);
            }
            LOGGER.log(Level.INFO, "Unexpected exception occurred", e);
        }
    }

    private MoUssdResp fireMoEvent(final MoUssdListener moUssdListener, final MoUssdReq moUssdReq) throws InterruptedException, ExecutionException {
        

        Future future = this.executorService.submit(new Callable() {
            public Object call() throws Exception{
                MoUssdResp ressss = null;
                try {
                    ressss = moUssdListener.onReceivedUssd(moUssdReq);
                } catch (Exception e) {
                    MoUssdReceiver.LOGGER.log(Level.INFO, "Unexpected error occurred ", e);
                }
                return ressss;
            }
        });

        return (MoUssdResp) future.get();
    }
    
    private MoUssdResp fireMoEventt(final MoUssdListener moUssdListener, final MoUssdReq moUssdReq, HttpServletRequest req) {
        final MoUssdResp ressss = null;

        this.executorService.submit(new Runnable() {
            public void run() {
                try {
                    moUssdListener.onReceivedUssd(moUssdReq);
                } catch (Exception e) {
                    MoUssdReceiver.LOGGER.log(Level.INFO, "Unexpected error occurred ", e);
                }
            }
        });

        return ressss;
    }

    private String readStringContent(HttpServletRequest req) throws IOException {
        InputStream is = req.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

        // MSISDN=720844418 & SERVICE_CODE=%2A678%23 & SESSIONID=1331013186 & INPUT_STRING=ffgh & netID=1
        String[] queryStringArray = req.getQueryString().split("&");

        String[] MSISDNArray = queryStringArray[0].split("=");
        String MSISDN = "254" + URLDecoder.decode(MSISDNArray[1], "UTF-8");
        
        String[] sessionArray = queryStringArray[2].split("=");
        String sessionID = URLDecoder.decode(sessionArray[1], "UTF-8");
        
        String[] inputStringArray = queryStringArray[3].split("=");
        String message = URLDecoder.decode(inputStringArray[1], "UTF-8");
        
        

        System.out.println("  >>>>>>>>>>  getRequestURL >> " + req.getRequestURL() + " " + req.getQueryString());
        
        

        // 
        return (new StringBuilder()).append("{").append("applicationId=\"").append("applicationId").append('\"').append(", version=\"").append("version").append('\"').append(", sessionId='").append(sessionID).append('\'').append(", ussdOperation='").append("ussdOperation").append('\'').append(", sourceAddress='").append("addr:" + MSISDN).append('\'').append(", vlrAddress='").append("vlrAddress").append('\'').append(", message='").append(message).append('\'').append(", encoding='").append("encoding").append('\'').append(", requestId='").append("requestId").append('\'').append('}').toString();

        /*StringBuilder content = new StringBuilder();
         String line;
         while ((line = bufferedReader.readLine()) != null) {
         content.append(line);
         System.out.println("readStringContent::: line " + line);
         }
         System.out.println("readStringContent::: " + content);
         System.out.println("readStringContent::: " + content.toString());
         return content.toString(); */
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("vvvvvvvvvvv  contentType ");
        resp.getWriter().println("SDP Application is Running");
    }
}
