
public class QueueLinkedList {
	DoublyLinkedList Queue;
	public QueueLinkedList(){
		Queue = new DoublyLinkedList();
	}
	public void enqueue(Object o){
		Queue.insertlast(o);
	}
	public Object dequeue(){
	    return Queue.removefirst();
	}
	public Object peek(){
		return Queue.getfirst();
	}
	public boolean isempty(){
		return Queue.isempty();
	}
	public int size(){
	    DoublyLinkedList tmp = new DoublyLinkedList();
	    int c=0;
	    while(!Queue.isempty()){
	    	tmp.insertlast(Queue.removefirst());
	    	c++;
	    }
	    while(!tmp.isempty()){
	    	Queue.insertlast(tmp.removefirst());
	    }
	    return c;
	}
	public void display(){
		Queue.displayforward();
	}
}