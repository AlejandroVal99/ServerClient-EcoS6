import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.Gson;

import processing.core.PApplet;

public class Main extends PApplet {

	private int screen;
	private Socket socket;
	private BufferedWriter writer;
	private BufferedReader reader;
	private ArrayList<User> usuarios = new ArrayList<User>();
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("Main");
	}
	
	public void settings() {
		
		size(500,500);
		
	}

	public void setup() {
		screen = 0;
		usuarios.add(new User("Alejandro","12345"));
		usuarios.add(new User("Nathalia","120616"));
		usuarios.add(new User("Pascal","wauwau"));
		initServer();
		
	}
	private void initServer() {
		
		new Thread(
				()->{
					
					try {
						ServerSocket server = new ServerSocket(5000);
						System.out.println("Esperando conexion");
						socket = server.accept();
						System.out.println("Cliente conectado");
						
						InputStream is = socket.getInputStream();
						InputStreamReader isr = new InputStreamReader(is);
						reader = new BufferedReader(isr);
						
						OutputStream os = socket.getOutputStream();
						OutputStreamWriter osw = new OutputStreamWriter(os);
						writer = new BufferedWriter(osw);
						
					
						while(true) {
							String line = reader.readLine();
							Gson gson = new Gson();
							User obj = gson.fromJson(line,User.class);
							System.out.println(obj.getId());
							boolean confirmation = verificarUser(obj);
							System.out.println("fue: " + confirmation);
							
							if(confirmation) {
								
								Comprobacion objeto = new Comprobacion(true);
								String json = gson.toJson(objeto);
								sendMessage(json);
								screen = 1;
								
							}else {
								Comprobacion objeto = new Comprobacion(false);
								String json = gson.toJson(objeto);
								sendMessage(json);
								
							}
							System.out.println("Recibido: " + line);
							
						}
						
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				).start();
		
		 
		
	}

	private boolean verificarUser(User obj) {
		
		
		for(int i = 0; i < usuarios.size();i++) {
			String passwordobj = obj.getPassword();
			String userobj = obj.getUsername();
			
			String passwordUser = usuarios.get(i).getPassword();
			String userUser = usuarios.get(i).getUsername();
		
			if(passwordobj.equals(passwordUser)&& userobj.equals(userUser)){
				return true;
			}
		}
		return false;
	}

	public void draw() {
		
		switch(screen) {
		case 0:
			background(0,96,255);
			textAlign(CENTER);
			textSize(18);
			text("Ingrese su usuario y contraseña desde "+"\n"+" su telefóno movil", height/2, width/2);
			break;
		case 1:
			background(0,96,255);
			textAlign(CENTER);
			textSize(24);
			text("Bienvenido", height/2, width/2);
			break;
		}
		
	}
	
	public void sendMessage(String msg) {
		new Thread(
				()->{
					try {
						writer.write(msg+"\n");
						writer.flush();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				).start();
		
		
	}
	
	
	
	
}
