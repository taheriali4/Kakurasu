package kakurasu;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

//source:
//https://medium.com/@ssaurel/create-a-simple-http-web-server-in-java-3fc12b29d5fd
//want to make a local server hosted webpage to play the game since js looks way nicer than swing
public class gui implements Runnable{
	
	static final File WEB_ROOT = new File(".");
	static final String DEFAULT_FILE = "index.html";
	static final String FILE_NOT_FOUND = "404.html";
	static final String UNSUPPORTED = "bad.html"; //change
	
	static final int PORT = 8080;
	
	static final boolean verbose = true;
	
	//client connection via socket
	private Socket connect;

	private Kakurasu k;
	public gui(Kakurasu k, Socket c) {
		this.connect = c;
		this.k = k;
	}
	@Override
	public void run() {
		BufferedReader in;
		PrintWriter out;
		BufferedOutputStream dataOut;
		String fileRequested;
		
		try {
			//get stuff from client
			in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
			//write things out to client via headers
			out = new PrintWriter(connect.getOutputStream());
			// get binary output stream to client (for requested data)
			dataOut = new BufferedOutputStream(connect.getOutputStream());
			
			//get first request line (client is requesting a file)
			String input = in.readLine();
			//parse request
			StringTokenizer parse = new StringTokenizer(input);
			String m = parse.nextToken().toUpperCase();
			//get file requested
			fileRequested = parse.nextToken().toLowerCase();
			
			if(!m.equals("GET") && !m.equals("HEAD")){
				System.err.println("bad method call: " + m.toString());
			}else {
				if(fileRequested.endsWith("/")) {
					fileRequested += DEFAULT_FILE;
				}
				
				File file = new File(WEB_ROOT, fileRequested);
				int fileLength = (int) file.length();
				//String content = getContentType(fileRequested);
			}
			
		}catch(FileNotFoundException f) {
			
		}catch(IOException e) {
			
		}
	}
	
	public static void startUp() {
		try {
			ServerSocket serverConnect = new ServerSocket(PORT);	
			Kakurasu k = new Kakurasu(4);
			while(true) {
				gui g = new gui(k, serverConnect.accept());

				if(verbose) {
					System.out.println("connection started " + new Date());
				}
				
				Thread t = new Thread(g);
				t.start();

			}
		}catch(IOException e) {
			System.err.println("server connection error" + e.getMessage());
			
		}
	}
}
