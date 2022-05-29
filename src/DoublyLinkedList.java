
class Link{
	public Object data;
	public Link next;
	public Link previous;
	public Link(Object o){
		data=o;
		next=null;
		previous=null;
	}
	public String toString(){
		return data.toString();
	}
}
public class DoublyLinkedList {
	private Link first;
	private Link last;
	public DoublyLinkedList(){
		first=null;
		last=null;
	}
	public boolean isempty(){
		return first==null;
	}
	public void insertfirst(Object o){
		Link l = new Link(o);
		if(isempty())
			last=l;
		else
			first.previous=l;
		l.next=first;
		first=l;	
	}
	public void insertlast(Object o){
		Link l = new Link(o);
		if(isempty())
			first=l;
		else{
			last.next=l;
			l.previous=last;
		}
		last=l;			
	}
	public Object removefirst(){
		Object tmp = first.data;
		if(first.next==null)
			last=null;
		else
			first.next.previous=null;
		first=first.next;
		return tmp;
	}
	public Object removelast(){
		Object tmp = last.data;
		if(first.next==null)
			first=null;
		else
			last.previous.next=null;
		last=last.previous;
		return tmp;
	}
	public Object getfirst(){
		return first.data;
	}
	public Object getlast(){
		return last.data;
	}
	public void displayforward(){
		System.out.print("[ ");
		Link current = first;
		while(current!=null){
			System.out.print(current+" ");
			current=current.next;
		}
		System.out.println("]");
	}
	public void displaybackward(){
		System.out.print("[ ");
		Link current = last;
		while(current!=null){
			System.out.print(current+" ");
			current=current.previous;
		}
		System.out.print("]");
	}
	public boolean insertafter(Object o, Object d){
		Link cur=first;
		while(!cur.data.equals(o)){
			cur=cur.next;
			if(cur==null)
				return false;
		}
		Link l = new Link(d);
		if(cur==last){
			cur.next=l;
			l.next=null;
			l.previous=cur;
			last=l;
		}
		else{
			l.next=cur.next;
			l.previous=cur;
			cur.next.previous=l;
			cur.next=l;			
		}
		return true;
	}
	public boolean insertbefore(Object o, Object d){
		Link cur=first;
		while(!cur.data.equals(o)){
			cur=cur.next;
			if(cur==null)
				return false;
		}
		Link l = new Link(d);
		if(cur==first){
			l.next=first;
			l.previous=null;
			first.previous=l;
			first=l;
		}
		else{
			l.next=cur;
			l.previous=cur.previous;
			cur.previous.next=l;
			cur.previous=l;
		}
		return true;
	}
	public Object delete(Object o){
		Link cur=first;
		while(!cur.data.equals(o)){
			cur=cur.next;
		    if(cur==null)
			    return false;
	    }
		if(cur==first){
			first=cur.next;
			cur.next.previous=null;
			cur.next=null;
			return cur.data;
		}
		if(cur==last){
			last=cur.previous;
			cur.previous.next=null;
			cur.previous=null;
			return cur.data;
		}
		else{
			cur.previous.next=cur.next;
			cur.next.previous=cur.previous;
			cur.next=null;
			cur.previous=null;
			return cur.data;
		}
	}
	public void inserttosorted(Comparable x){
		Link cur=first;
		Link tmp = new Link(x);
		if(first==null){
			first=tmp;
			last=tmp;
			return;
		}
		while(cur!=null){
			if(x.compareTo(cur.data)<0){
				if(cur.previous!=null){
					tmp.next=cur;
					tmp.previous=cur.previous;
					cur.previous.next=tmp;
					cur.previous=tmp;
					break;		
				}
				else{
					tmp.next=cur;
					tmp.previous=null;
					cur.previous=tmp;
					first=tmp;
					break;
				}
			}
			if(cur.next==null){
				cur.next=tmp;
				tmp.previous=cur;
				last=tmp;
				break;
			}
			cur=cur.next;
		}
	}
	public void insertionsort(){
		DoublyLinkedList tmp = new DoublyLinkedList();
		Link cur=first;
		while(cur!=null){
			tmp.inserttosorted((Comparable)cur.data);
			cur=cur.next;
		}
		first=tmp.first;
	    last=tmp.last;
	}
	public void reverse(){
		Link cur=first;
		while(cur!=null){
		    if(cur.previous==null)
			    last=cur;
		    if(cur.next==null)
		    	first=cur;
		    Link tmp = cur.previous;
		    cur.previous=cur.next;
		    cur.next=tmp;
		    cur=cur.previous;
		}
	}
	public void reverserec(){
		reverserechelper(first);
	}
	public void reverserechelper(Link l){
		if(l!=null){
			if(l.previous==null)
				last=l;
			if(l.next==null)
				first=l;
			Link tmp = l.previous;
			l.previous=l.next;
			l.next=tmp;
			reverserechelper(l.previous);
		}
	}
	public int countiter(){
		int n=0;
		for(Link x = first;x!=null;x=x.next)
			n++;
		return n;
	}	
}
