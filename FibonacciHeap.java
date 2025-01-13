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
	public int TotalLinks;
	public int TotalCuts;
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
		TotalLinks = TotalCuts =0;
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
		FibonacciHeap.HeapNode.concatenate(min, node);
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
		TotalCuts +=min.rank;
		if (min.next == min)
		{
			 min = null;
			 size--;
			 return;
		}
		
		HeapNode tempmin = min.next;
		
		this.Rootify(min);
		
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
				
				Linked =  FibonacciHeap.HeapNode.Link(heap, buckets[currRank], this);
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
		x.key = x.key - diff;
		if(x.key <x.parent.key)//if Heap rule is broken.
		{
			FibonacciHeap.HeapNode.CascadingCut(x, x.parent, this);
		}
		
		
		
	}

	
	
	/**
	 * 
	 * Delete the x from the heap.
	 *
	 */
	public void delete(HeapNode x) 
	{   
		
		if(x==min)
			this.deleteMin();
		else
		{
			//change here
			HeapNode currmin = min;
			this.decreaseKey(x, Integer.MAX_VALUE);
			TotalCuts +=x.rank;
			min = currmin;
			this.Rootify(x);
			
			size--;
		}
		
		
	}

	public void Rootify (HeapNode x)
	{
		
//		if(x.parent == null)
//			numOfTrees--;
//		else
//		{
//			x.parent.rank--;
//			if(x.next==x)
//				x.parent.child =null;
//			else
//				x.parent.child = x.next;
//		}
		
		numOfTrees--;
		
		HeapNode temp = x.child;
		HeapNode curr;
		
		for (int i=0;i<x.rank;i++)
		{
			curr = temp.next;
			temp.next = temp;
			temp.prev = temp;
			this.insert(temp);
			
			temp = curr;

		}
		x.prev.next = x.next;
		x.next.prev = x.prev;
	}
	/**
	 * 
	 * Return the total number of links.
	 * 
	 */
	public int totalLinks()
	{
		return TotalLinks; // should be replaced by student code
	}


	/**
	 * 
	 * Return the total number of cuts.
	 * 
	 */
	public int totalCuts()
	{
		return TotalCuts; // should be replaced by student code
	}


	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(FibonacciHeap heap2)
	{
		
		//push heap2 between heap's min and it's next node.
		HeapNode tempmin = heap2.min; 
		HeapNode Nextmin = min.next;
		
		Nextmin.prev = tempmin.prev;
		tempmin.prev.next = Nextmin;
		min.next = tempmin;
		tempmin.prev = min;
		//update new min.
		if(heap2.min.key<min.key)
			min = heap2.min;
		//update fields.			
		TotalLinks +=heap2.TotalLinks;
		TotalCuts +=heap2.TotalCuts;
		size +=heap2.size;
		numOfTrees+=heap2.numOfTrees;
		

		
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
		public static HeapNode Link (HeapNode first, HeapNode second, FibonacciHeap heap)
		{
			//we assume second is smaller
			if (first.key<second.key)
				return Link(second, first,heap);
			
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
			heap.TotalLinks++;
			return second;
	
		}
		
		public static void Cut (HeapNode x, HeapNode y,FibonacciHeap heap) //cut x from parent y
		{
			heap.TotalCuts++;
	
			x.parent = null;
			x.mark = false;
			y.rank--;
			
			if (x.next ==x)
			{
				y.child = null;
			}
			
			else 
			{
				y.child = x.next;
				x.prev.next = x.next;
				x.next.prev = x.prev;
			}
		}
		
		
		public static void CascadingCut(HeapNode x, HeapNode y, FibonacciHeap heap)//cascading cut prcoess starting with node x.
		{
			Cut(x,y,heap);
			heap.insert(x);
			if(y.parent !=null)
			{
				if(y.mark == false)
					y.mark = true;
				else
					CascadingCut(y, y.parent, heap);
			}
			
		}

		private static void printHeap(HeapNode root, int numOfTrees) {
		    if (root == null || numOfTrees == 0) return;
		 
		    System.out.println("Heap Structure:");
		 
		    HeapNode current = root;
		    int count = 0;
		 
		    // Traverse up to numOfTrees
		    while (count < numOfTrees) {
		        // Print the root of the current tree
		        System.out.println((count == numOfTrees - 1 ? "└── " : "├── ") + "Tree rooted at " + current.key);
		 
		        // Print the tree structure recursively
		        printTree(current, "    ", true);
		 
		        // Move to the next root in the circular list
		        current = current.next;
		        count++;
		    }
		}
		 
		private static void printTree(HeapNode node, String indent, boolean isLast) {
		    if (node == null) return;
		 
		    // Print the current node
		    System.out.println(indent + (isLast ? "└── " : "├── ") + node.key);
		 
		    // Update indentation for children
		    String childIndent = indent + (isLast ? "    " : "│   ");
		 
		    // Traverse and print children
		    HeapNode child = node.child;
		    if (child != null) {
		        HeapNode start = child;
		        int childCount = 0;
		 
		        do {
		            // Determine if the current child is the last in its sibling list
		            boolean isChildLast = (child.next == start);
		            printTree(child, childIndent, isChildLast);
		            child = child.next;
		            childCount++;
		        } while (child != start); // Stop when we loop back to the start
		    }
		}
		
		
	}
	
	public static void main(String[]args)
	{
		FibonacciHeap heapi = new FibonacciHeap();
		for (int i=1;i<10;i++)
		heapi.insert(2*i, "hi");

		

		heapi.deleteMin();

		HeapNode mini =  heapi.findMin().child.prev.child;
		for(int i=0;i<3;i++)
		{
		System.out.println(mini.key);
		mini = mini.next;
		}
		FibonacciHeap.HeapNode.printHeap(heapi.findMin(), heapi.numOfTrees);
		heapi.delete(heapi.findMin().child.next.child.next);
		System.out.println("after del");
		heapi.delete(heapi.findMin().child.next.child);
		System.out.println("total cuts:" + heapi.totalCuts());
		FibonacciHeap.HeapNode.printHeap(heapi.findMin(),heapi.numOfTrees);
		heapi.deleteMin();
		FibonacciHeap.HeapNode.printHeap(heapi.findMin(),heapi.numOfTrees);
		System.out.println(heapi.TotalLinks);
		//heapi.decreaseKey(heapi.findMin().child.next, 5);
		FibonacciHeap.HeapNode.printHeap(heapi.findMin(),heapi.numOfTrees);
		System.out.println(heapi.totalCuts());
		
		FibonacciHeap heap2 = new FibonacciHeap();
		for (int i=1;i<10;i++)
			heap2.insert(20*i, "hi");
		FibonacciHeap.HeapNode.printHeap(heap2.findMin(),heap2.numOfTrees);
		heapi.meld(heap2);
		FibonacciHeap.HeapNode.printHeap(heapi.findMin(),heapi.numOfTrees);
	}
}
