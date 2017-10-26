/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpango.ussd;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author jmulutu
 */
public class GenericSender<Req, Resp> {

    private URL sdpUrl;

    public GenericSender(URL sdpUrl) {
        this.sdpUrl = sdpUrl;
    }

    public Resp sendRequest(Req req, Class<Resp> resp)
            throws SdpException {
        try {
            return (Resp) internalSendRequest(req, resp);
        } catch (Exception e) {
            throw new SdpException(e);
        }
    }

    private Resp internalSendRequest(Req req, Class<Resp> resp)
            throws IOException {
        URLConnection conn = this.sdpUrl.openConnection();
        Gson gson = new Gson();
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

        wr.write(gson.toJson(req));
        wr.flush();

        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            content.append(line);
            content.append("\n");
        }
        Resp response = gson.fromJson(content.toString(), resp);
        wr.close();
        rd.close();
        return response;
    }

    public URL getSdpUrl() {
        return this.sdpUrl;
    }

    public void setSdpUrl(URL sdpUrl) {
        this.sdpUrl = sdpUrl;
    }
}
