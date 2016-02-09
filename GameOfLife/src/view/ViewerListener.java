
package view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
/**
 * 
 * ViewerListener observes, and  sees if any changes are detected to the view. Changes that are detected are the changes
 * to the board. The height and width of the board, as well as the size of canvas. The placement of the board is also detected.
 * 
 * @author Ali Arfan

 *
 */

 public class ViewerListener {
	
	private ChangeListener<Number> listenerWidth;
	private ChangeListener<Number> listenerHeight;
	
	private int width = 0;
	private int height = 0;
	
	private ViewerCenterBoard obj;
	/**
	 *
	 * <blockquote>
	 * <pre>{@code public ViewerListener({@link Canvas} canvas, {@link ViewerCenterBoard} obj)}</pre>
	 * <p> The constructor of the class where the canvas is set, and the the centerboard is set. The changes are observed
	 * and if there are any changes, the height, width  and the placement of the board is changed and notified. </p>
	 * @param canvas - The canvas that is used on the board.
	 * @param obj -  The object of the ViewerCenterBoard that is used to center the board.¨
	 * </blockquote>
	 * 
	 */
	
	public ViewerListener(Canvas canvas, ViewerCenterBoard obj) {
		this.obj = obj;
		
		listenerWidth = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				width = newValue.intValue();
				doOperation();
			}
		};
		
		listenerHeight = new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				height = newValue.intValue();
				doOperation();
			}
		};
		
		canvas.widthProperty().addListener(listenerWidth);
		canvas.heightProperty().addListener(listenerHeight);
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>listenerWidth</i></b>
	 * <pre>{@code public ChangeListener<Number> listenerWidth() }</pre>
	 * <p> Returns the size value of how much the width of the canvas has changed.</p>
	 * @return listenerWidth -  The size value of how much the canvas width has changed.
	 * </blockquote>
	 * 
	 */
	
	public ChangeListener<Number> listenerWidth() {
		return listenerWidth;
	}
	
	/**
	 *
	 * <blockquote>
	 * <b><i>listenerHeight</i></b>
	 * <pre>{@code public ChangeListener<Number> listenerHeight() }</pre>
	 * <p> Returns the size value of how much the height of the canvas has changed.</p>
	 * @return listenerHeight -  The size value of how much the canvas height has changed.
	 * </blockquote>
	 * 
	 */
	
	public ChangeListener<Number> listenerHeight() {
		return listenerHeight;
	}	
	
	/**
	 *
	 *<blockquote>
	 * <b><i>getViewWidth</i></b>
	 * <pre>{@code public int getViewWidth()}</pre>
	 * <p>Returns the current width of the view, the width of the area inside the border.</p>
	 * @return width -  The width of the view.
	 * </blockquote>
	 * 
	 */
	public int getViewWidth() {
		return width;
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>getViewHeight</i></b>
	 * <pre>{@code public int getViewHeight()}</pre>
	 * <p> Returns the current height of the view, the height of the area inside the border.</p>
	 * @return height -  The height of the view.
	 * </blockquote>
	 * 
	 */
	
	public int getViewHeight() {
		return height;
	}
	
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>doOperation</i></b>
	 * <pre>{@code private void doOperation()}</pre>
	 * <p> If the width and height is not zero, the wiewSize object is set to the current widths and heights</p>
	 * </blockquote>
	 * 
	 */
	private void doOperation() {
		if(width != 0 && height != 0) {
			obj.viewsizeContext(this);
		}
	}
}
