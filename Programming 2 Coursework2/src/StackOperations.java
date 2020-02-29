import java.util.Stack;

public class StackOperations {
	
	public static Stack<MyRectangle> stackUndo = new Stack<MyRectangle>();
	public static Stack<MyRectangle> stackRedo = new Stack<MyRectangle>();
	
	public static void push(MyRectangle cell) {
		StackOperations.stackUndo.push(cell);
		System.out.println(stackUndo.size());
	}
	
	public static MyRectangle undo() {
		if(!stackUndo.isEmpty()) {
			stackRedo.push(stackUndo.peek());
		}
		return StackOperations.stackUndo.pop();
	}
	
	public static MyRectangle redo() {
		if(!stackRedo.isEmpty()) {
			stackUndo.push(stackRedo.peek());			
		}
		return StackOperations.stackRedo.pop();
	}
	
}