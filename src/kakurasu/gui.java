package kakurasu;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
//want to make a local server hosted webpage to play the game since jt looks way nicer than swing
public class gui implements Runnable{
	
	static final File WEB_ROOT = new File(".");
	static final String DEFAULT_FILE = "index.html";
	static final String FILE_NOT_FOUND = "404.html";
	static final String UNSUPPORTED = "bad.html"; //change
	
	static final int PORT = 8080;
	
	static final boolean verbose = true;
	
	//client connection via socket
	private Socket connect;

	public gui(Socket c) {
		this.connect = c;
	}
	@Override
	public void run() {
		BufferedReader in = null;
 		PrintWriter out = null;
		BufferedOutputStream dataOut = null;
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
			System.out.println(fileRequested);
			
			if(!m.equals("GET") && !m.equals("HEAD")){
				System.err.println("bad method call: " + m.toString());
			}else {
				if(fileRequested.startsWith("/puzzle?")){
					int size = Integer.parseInt(fileRequested.substring(fileRequested.lastIndexOf("?") + 1));
					System.out.println("puzzle requested of size " + size);
					Kakurasu k = new Kakurasu(size);

                    String puzzle = k.getRow() + k.getCol();
					
					int fileLength = (int) puzzle.length();
					
					out.println("HTTP/1.1 200 OK");
					out.println("Server: Java HTTP Server from poogs : 1.0");
					out.println("Date: " + new Date());	
					out.println("content-type: puzzle");
					out.println("Content-length" + k.toString().length());
					out.println();
					out.flush();
					
					byte[] output = puzzle.getBytes();
					dataOut.write(output, 0, fileLength);
					dataOut.flush();
					
					
				}else {

				if(fileRequested.endsWith("/")) {
					fileRequested += DEFAULT_FILE;
				}
				
				
				File file = new File(WEB_ROOT, fileRequested);
				int fileLength = (int) file.length();
				String content = getContentType(fileRequested);
				
				if(m.equals("GET")){
					byte[] fileData = readFileData(file, fileLength);
					
					out.println("HTTP/1.1 200 OK");
					out.println("Server: Java HTTP Server from SSaurel : 1.0");
					out.println("Date: " + new Date());
					out.println("Content-type: " + content);
					out.println("Content-length: " + fileLength);
					out.println(); // blank line between headers and content, very important !
					out.flush(); // flush character output stream buffer
					
					dataOut.write(fileData, 0, fileLength);
					dataOut.flush();
					
				}
				
				if (verbose) {
					System.out.println("File " + fileRequested + " of type " + content + " returned");
				}
				
				}
			}
			
		}catch(FileNotFoundException f) {
			System.err.println("FNF err: " + f.getMessage());
		}catch(IOException e) {
			System.err.println("error: " + e.getMessage());
			
		}
		finally {
			try {
				in.close();
				out.close();
				dataOut.close();
				connect.close(); //close socket connection
			}catch(Exception e) {
				System.err.println("Error closing stream : " + e.getMessage());
			} 
			
			if (verbose) {
				System.out.println("Connection closed.\n");
			}
		}
	}
	
	private byte[] readFileData(File f, int fileLength) throws IOException{
		FileInputStream fileIn = null;
		byte[] data = new byte[fileLength];
		try {
			fileIn = new FileInputStream(f);
			fileIn.read(data);
		}finally {
			if(fileIn != null) {
				fileIn.close();
			}
		}
		return data;
	}
	
	private String getContentType(String fileReq) {
		if(fileReq.endsWith(".htm") || fileReq.endsWith(".html")) {
			return "text/html";
		}else if(fileReq.endsWith(".css")){
            return "text/css";
        }else{
			return "text/plain";
		}
	}
	
	
	public static void startUp() {
		try {
			ServerSocket serverConnect = new ServerSocket(PORT);	
			while(true) {
				gui g = new gui(serverConnect.accept());

				if(verbose) {
					System.out.println("connection started " + new Date());
				}
				
				Thread t = new Thread(g);
				t.start();

			}
		}catch(IOException e) {
			System.err.println("server connection error " + e.getMessage());
			
		}
	}
}
