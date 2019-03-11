package Examples;
	// Java code for stack implementation 

public class MyStack {
   private int maxSize;
   private String[] stackArray;
   private int top;
   
   public MyStack(int s) {
      maxSize = s;
      stackArray = new String[maxSize];
      top = -1;
   }
   public void push(String j) {
      stackArray[++top] = j;
   }
   public String pop() {
      return stackArray[top--];
   }
   public String peek() {
      return stackArray[top];
   }
   public boolean isEmpty() {
      return (top == -1);
   }
   public boolean isFull() {
      return (top == maxSize - 1);
   }
   public static void main(String[] args) {
      MyStack theStack = new MyStack(10); 
      theStack.push("A");
      theStack.push("B");
      theStack.push("C");
      theStack.push("D");
      theStack.push("E");
      
      while (!theStack.isEmpty()) {
         String value = theStack.pop();
         System.out.print(value);
         System.out.print(" ");
      }
      System.out.println("");
   }
}