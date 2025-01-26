/**
 * FibonacciHeap
 *
 * An implementation of Fibonacci heap over positive integers.
 *
 *username: elinoyr, id:206433252
 *username: yairkohn1, id: 212498406
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
	//Time complexity: O(1) wc.
	public FibonacciHeap()
	{
		
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
	//Time Complexity: O(1) wc.
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
	//Time Complexity O(1) wc.
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
	//Time Complexity: O(1) w.c.
	public HeapNode findMin()
	{
		return min;
	}

	/**
	 * 
	 * Delete the minimal item
	 *
	 */
	//Time complexity: O(n) w.c, O(log n) amortized.
	public void deleteMin()
	{
		if (min==null)
			return;
		TotalCuts +=min.rank;
		if (size==1)
		{
			 min = null;
			 numOfTrees--;
			 size--;
			 return;
		}
		HeapNode tempmin;
		if (this.numOfTrees!=1)
		{
		 tempmin = min.next;
		}
		else
		{
		 tempmin = min.child;
		}
		
		this.Rootify(min);
		
		Consolidating(tempmin);
		size --;
	}

	//Time complexity: O(n) wc and O(log n) amortized.
	public void Consolidating(HeapNode heap)
	{
		HeapNode Linked = null;
		HeapNode curr;
		int currRank;
		int maxRank = (int) Math.ceil(Math.log(2.5*size) / Math.log(1.61)) + 1; //max rank based on class proof upperbound.
		HeapNode[] buckets = new HeapNode[maxRank];
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
	//Time complexity: O(n) wc, O(1) amortized.
	public void decreaseKey(HeapNode x, int diff) 
	{    
		x.key = x.key - diff;
		if(x.parent==null && x.key<min.key)
			min = x;
			
		if(x.parent!=null && x.key <x.parent.key)//if Heap rule is broken.
		{
			FibonacciHeap.HeapNode.CascadingCut(x, x.parent, this);
		}
		
		
		
	}

	
	
	/**
	 * 
	 * Delete the x from the heap.
	 *
	 */
	//Time Complexity: O(n) worst case, O(log n) amortized.
	public void delete(HeapNode x) 
	{   

		if(x==min)
			this.deleteMin();
		else
		{
			
			HeapNode currmin = min;
			this.decreaseKey(x, Integer.MAX_VALUE);
			TotalCuts +=x.rank;
			
			this.Rootify(x);
						
			min = currmin;
			size--;
		}
		
		
	}

	//time complexity: O(log n) w.c.
	public void Rootify (HeapNode x)
	{
		

		
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
	//Time complexity: O(1) wc.
	public int totalLinks()
	{
		return TotalLinks; 
	}


	/**
	 * 
	 * Return the total number of cuts.
	 * 
	 */
	//Time complexity: O(1) wc.
	public int totalCuts()
	{
		return TotalCuts; 
	}


	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	//Time complexity: O(1) wc.
	public void meld(FibonacciHeap heap2)
	{
		if(heap2 ==null  )
			return;
		if(heap2.min == null)
			return;
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
	//Time complexity: O(1) w.c
	public int size()
	{
		return size; 
	}


	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	//Time complexity: O(1) w.c
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
			//Time complexity: O(1) wc.
			HeapNode nexttemp =first.next;
			first.next = second; 
			second.next = nexttemp;
			second.prev = first; 
			nexttemp.prev = second;
			
		}
		//pre: first and second have same rank.
		//Time complexity: O(1) wc.
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
			second.child = first; //keeps child n1 ranked.
			first.parent = second;
			second.rank++;
			heap.TotalLinks++;
			return second;
	
		}
		//Time complexity: O(1) wc.
		public static void Cut (HeapNode x, HeapNode y,FibonacciHeap heap) //cut x from parent y
		{
			heap.TotalCuts++;
	
			x.parent = null;
			x.mark = false;
			y.rank--;
			
			if (x.next ==x)//keeps child the n.1 ranked.
			{
				y.child = null;
			}
			
			else 
			{
				if(y.child ==x)
				{
					y.child = x.prev;
				}
				x.prev.next = x.next;
				x.next.prev = x.prev;
			}
		}
		
		//Time complexity: O(n) W.c, O(1) amortized.
		public static void CascadingCut(HeapNode x, HeapNode y, FibonacciHeap heap)//cascading cut prcoess starting with node x.
		{
			Cut(x,y,heap);
			heap.insert(x);
			if(y.parent !=null)
			{
				if(y.mark == false)
				{
					y.mark = true;
				}
				else
				{	
					CascadingCut(y, y.parent, heap);
				}
			}
			
		}

		
		
		
	}
	
}
