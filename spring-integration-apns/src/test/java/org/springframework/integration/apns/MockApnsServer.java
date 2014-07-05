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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.relayrides.pushy.apns.ExpiredToken;

public class MockApnsServer extends Thread {
	private int port;
	private boolean shutDown = false;
	private List<ExpiredToken> expiredTokens;
	

	public MockApnsServer(int port) {
		this.port = port;
	}

	public void run() {
		PrintWriter out = null;
		Socket socket = null;
		try {
			System.out.println("Starting mock server.....");
			ServerSocket listener = new ServerSocket(port);
			socket = listener.accept();
			out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(
	                new InputStreamReader(socket.getInputStream()));
			while (!shutDown) {
				String input = in.readLine();
				System.out.println(input);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(socket);
		}
	}
	
	public void shutDown() {
		System.out.println("Shutting down mock server.....");
		this.shutDown = true;
	}


	

}
