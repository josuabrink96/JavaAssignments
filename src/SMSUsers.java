import java.io.IOException;
import java.nio.channels.SocketChannel;

public class SMSUsers extends Thread implements Observer {
	private final Subject subject;
	private String desc;
	private String userInfo;
	private SocketChannel socketChannel;
	private Observer observer;
	
	public SMSUsers(Subject subject,String userInfo, SocketChannel channel) {
		if(subject==null){
			throw new IllegalArgumentException("No Publisher found.");
		}
		this.subject = subject;
		this.userInfo = userInfo;
		this.socketChannel = channel;

		observer = SMSUsers.this;
		observer.subscribe();
		observer.update("Generic Soccer Match\n\rPress unsubscribe to unsubscribe, or subscribe to subscribe");
	}

	@Override
	public void update(String desc) {
		this.desc = desc;
		display();
	}

	public void run() {
		while(true) {
			String message = HelperMethods.receiveMessage(socketChannel);
			if(message.equalsIgnoreCase("1")) {
				observer.update("Unsubscribed");
				observer.unSubscribe();
			}
			if(message.equalsIgnoreCase("2")) {
				observer.update("Subscribed");
				observer.subscribe();
			}
			if(message.equalsIgnoreCase("quit")) {
				observer.unSubscribe();
				break;
			}
		}
	}

	private void display(){
		HelperMethods.sendMessage(socketChannel, "["+userInfo+"]: "+desc+"\n\r");
	}
	
	@Override
	public void subscribe() {
		System.out.println("Subscribing "+userInfo+" to "+subject.subjectDetails()+" ...");
		this.subject.subscribeObserver(this);
		System.out.println("Subscribed successfully.");
	}

	@Override
	public void unSubscribe() {
		System.out.println("Unsubscribing "+userInfo+" to "+subject.subjectDetails()+" ...");
		this.subject.unSubscribeObserver(this);
		System.out.println("Unsubscribed successfully.");
	}

}
