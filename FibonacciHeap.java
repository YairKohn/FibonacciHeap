/**
 * FibonacciHeap
 *
 * An implementation of Fibonacci heap over positive integers.
 *
 */
public class FibonacciHeap
{
	public HeapNode min;
	public int size;
	public int numOfTrees;
	/**
	 *
	 * Constructor to initialize an empty heap.
	 *
	 */
	public FibonacciHeap()
	{
		// should be replaced by student code
		min = null;
		size = 0;
		numOfTrees = 0;
	}

	/**
	 * 
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapNode.
	 *
	 */
	public HeapNode insert(int key, String info) 
	{    
		HeapNode newNode = new HeapNode(key, info);
	
		if(min==null) //if Heap is empty.
		{
			min = newNode;
			
		}
		
		else
		{
			//concatenate the new node.
			FibonacciHeap.HeapNode.concatenate(min, newNode);
			if (newNode.key<min.key) //update min.
				min = newNode;
		}
		size++;
		numOfTrees++;
		return newNode; 
	}
	
	public void insert(HeapNode node)
	{
		if(min == null)
		{
			min = node;
			min.next = min;
			min.prev = min;
		}
		else
		{
		FibonacciHeap.HeapNode.concatenate(min, min.next);
		if (node.key<min.key)
			min = node;
		}
		node.parent = null;
		node.mark = false;
		numOfTrees++;
		
		
		
		
	}

	/**
	 * 
	 * Return the minimal HeapNode, null if empty.
	 *
	 */
	public HeapNode findMin()
	{
		return min;
	}

	/**
	 * 
	 * Delete the minimal item
	 *
	 */
	public void deleteMin()
	{
		if (min.next == min)
		{
			 min = null;
			 size--;
			 return;
		}
		
		HeapNode tempmin = min.next;
		
		min.prev.next = min.next;
		min.next.prev = min.prev;
		numOfTrees--;
		HeapNode temp = min.child;
		HeapNode curr;
		
		for (int i=0;i<min.rank;i++)
		{
			curr = temp.next;
			temp.next = temp;
			temp.prev = temp;
			this.insert(temp);
			
			temp = curr;

		}
		
		Consolidating(tempmin);
		size --;
	}

	
	public void Consolidating(HeapNode heap)
	{
		HeapNode Linked = null;
		HeapNode curr;
		int currRank;
		HeapNode[] buckets = new HeapNode[size+1];
		for (int i=0; i<numOfTrees;i++)
		{
			currRank = heap.rank;
			if (buckets[currRank]==null)
			{
				buckets[currRank] = heap;
				heap = heap.next;
			}
			else
			{
				curr =heap.next;
				while(buckets[currRank] != null)
				{
				
				Linked =  FibonacciHeap.HeapNode.Link(heap, buckets[currRank]);
				heap = Linked;
				buckets[currRank] = null;
				currRank++;
				
				}
				buckets[currRank] = Linked;
				heap = curr;
			}
			
		}
		
		FibonacciHeap newHeap = new FibonacciHeap();
		for (HeapNode h:buckets)
		{
			if(h != null)
			newHeap.insert(h);
		}
		this.min = newHeap.min;
		this.numOfTrees = newHeap.numOfTrees;
		
		
		
	}
	/**
	 * 
	 * pre: 0<diff<x.key
	 * 
	 * Decrease the key of x by diff and fix the heap. 
	 * 
	 */
	public void decreaseKey(HeapNode x, int diff) 
	{    
		return; // should be replaced by student code
	}

	/**
	 * 
	 * Delete the x from the heap.
	 *
	 */
	public void delete(HeapNode x) 
	{    
		return; // should be replaced by student code
	}


	/**
	 * 
	 * Return the total number of links.
	 * 
	 */
	public int totalLinks()
	{
		return 0; // should be replaced by student code
	}


	/**
	 * 
	 * Return the total number of cuts.
	 * 
	 */
	public int totalCuts()
	{
		return 0; // should be replaced by student code
	}


	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(FibonacciHeap heap2)
	{
		return; // should be replaced by student code   		
	}

	/**
	 * 
	 * Return the number of elements in the heap
	 *   
	 */
	public int size()
	{
		return size; // should be replaced by student code
	}


	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees()
	{
		return numOfTrees;
	}

	/**
	 * Class implementing a node in a Fibonacci Heap.
	 *  
	 */
	public static class HeapNode{
		public int key;
		public String info;
		public HeapNode child;
		public HeapNode next;
		public HeapNode prev;
		public HeapNode parent;
		public int rank;
		public boolean mark;
		
		public HeapNode(int k, String str)
		{
			key = k;
			info = str;
			rank = 0;
			mark = false;
			parent = null;
			child = null;
			next = this;
			prev = this;
			
		}
		
		public static void concatenate(HeapNode first, HeapNode second)
		{
			//connect the node(circular representation).
			HeapNode nexttemp =first.next;
			first.next = second; 
			second.next = nexttemp;
			second.prev = first; 
			nexttemp.prev = second;
			
		}
		//pre: first and second have same rank.
		public static HeapNode Link (HeapNode first, HeapNode second)
		{
			//we assume second is smaller
			if (first.key<second.key)
				return Link(second, first);
			
			first.next = first;
			first.prev = first;
			if (first.rank ==0)//if they are both single nodes.
			{
				second.child = first;
			}
			else
			{
				//concatenate first into second's children.
				concatenate(second.child, first);
				
			}
			
			//update pointer.
			first.parent = second;
			second.rank++;
			return second;
	
		}
		
	    public static void printHeap(HeapNode node) {
	        if (node == null) {
	            System.out.println("Heap is empty.");
	            return;
	        }

	        System.out.println("Heap structure:");
	        printTree(node, "", true);
	    }

	    private static void printTree(HeapNode node, String indent, boolean isLast) {
	        if (node == null) return;

	        // Print current node
	        System.out.println(indent + (isLast ? "└── " : "├── ") + node.key);

	        // Update indentation for children
	        String childIndent = indent + (isLast ? "    " : "│   ");

	        // Print children (iterate over the circular doubly linked list)
	        HeapNode child = node.child;
	        if (child != null) {
	            HeapNode start = child;
	            do {
	                boolean isChildLast = (child.next == start);
	                printTree(child, childIndent, isChildLast);
	                child = child.next;
	            } while (child != start);
	        }
	    }
		
		
	}
	
	public static void main(String[]args)
	{
		FibonacciHeap heapi = new FibonacciHeap();
		for (int i=1;i<10;i++)
		heapi.insert(i, "hi");

		

		heapi.deleteMin();

		HeapNode mini =  heapi.findMin().child.prev.child;
		for(int i=0;i<3;i++)
		{
		System.out.println(mini.key);
		mini = mini.next;
		}
		FibonacciHeap.HeapNode.printHeap(heapi.findMin());
	}
}
