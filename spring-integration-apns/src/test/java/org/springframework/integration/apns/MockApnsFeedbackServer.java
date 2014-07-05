/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.apns;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.relayrides.pushy.apns.ExpiredToken;

public class MockApnsFeedbackServer extends Thread {

	private int port;
	private List<ExpiredToken> expiredTokens;

	public MockApnsFeedbackServer(int port, List<ExpiredToken> expiredTokens) {
		this.port = port;
	}
	
	
	public void run()  {
		ServerSocket listener = null;
		Socket socket = null;
		PrintWriter out = null;
		try {
		listener = new ServerSocket(port);
		socket = listener.accept();
		out = new PrintWriter(socket.getOutputStream(), true);
			if (expiredTokens != null && !expiredTokens.isEmpty()) {
				for (ExpiredToken expiredToken : expiredTokens) {
					out.write((int) (expiredToken.getExpiration().getTime() / 1000L));
					out.write((short) expiredToken.getToken().length);
					out.write(new String(expiredToken.getToken()).toCharArray());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(socket);
		}

	}

}
