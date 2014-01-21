/**
 * Copyright (c) 2011, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.labs.internal.scene.control.skin;

import java.util.List;
import com.sun.javafx.css.converters.EnumConverter;
import java.util.ArrayList;
import java.util.Collections;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import jfxtras.labs.scene.layout.VBox;
import jfxtras.labs.scene.layout.HBox;
import javafx.util.Duration;
import jfxtras.labs.animation.Timer;
import jfxtras.labs.internal.scene.control.behavior.ListSpinnerBehavior;
import jfxtras.labs.scene.control.ListSpinner;
import jfxtras.labs.scene.layout.GridPane;

/**
 * 
 * @author Tom Eugelink
 * 
 * Possible extension: drop down list or grid for quick selection
 */
public class ListSpinnerCaspianSkin<T> extends SkinBase<ListSpinner<T>>
{
	// TODO: vertical centering 
	
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public ListSpinnerCaspianSkin(ListSpinner<T> control)
	{
		super(control);//, new ListSpinnerBehavior<T>(control));
		listSpinnerBehavior = new ListSpinnerBehavior<T>(control);
		construct();
	}
	final private ListSpinnerBehavior<T> listSpinnerBehavior;

	/*
	 * 
	 */
	private void construct()
	{
            // setup component
            createNodes();

            // react to value changes in the model
            getSkinnable().editableProperty().addListener(new ChangeListener<Boolean>()
            {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2)
                    {
                            replaceValueNode();
                    }
            });
            replaceValueNode();

            // react to value changes in the model
            getSkinnable().valueProperty().addListener(new ChangeListener<T>()
            {
                    @Override
                    public void changed(ObservableValue<? extends T> observableValue, T oldValue, T newValue)
                    {
                            refreshValue();
                    }
            });
            refreshValue();

            // react to value changes in the model
            setArrowCSS();
            layout();

            // react to value changes in the model
            alignValue();		
	}
	
	/*
	 * 
	 */
	private void refreshValue() 
	{
            // if editable
            if (getSkinnable().isEditable() == true)
            {
                // update textfield
                T lValue = getSkinnable().getValue();
                textField.setText( getSkinnable().getPrefix() + getSkinnable().getStringConverter().toString(lValue) + getSkinnable().getPostfix() );
            }
            else
            {
                // get node for this value
                Node lNode = getSkinnable().getCellFactory().call( getSkinnable() );
            }
	}
	
	// ==================================================================================================================
	// StyleableProperties
	
        /**
         * arrowPosition
         */
        public final ObjectProperty<ArrowPosition> arrowPositionProperty() 
        {
            if (arrowPosition == null) 
            {
                arrowPosition = new StyleableObjectProperty<ArrowPosition>(ArrowPosition.TRAILING) 
                {
                    @Override public void invalidated()
                    {
                        setArrowCSS();
                        layout();
                    }
                    
                    @Override public CssMetaData<ListSpinner,ArrowPosition> getCssMetaData() { return StyleableProperties.ARROW_POSITION; }
                    @Override public Object getBean() { return ListSpinnerCaspianSkin.this; }
                    @Override public String getName() { return "arrowPosition"; }
                };
            }
            return arrowPosition;
        }
        private ObjectProperty<ArrowPosition> arrowPosition = null;
        public final void setArrowPosition(ArrowPosition value) { arrowPositionProperty().set(value); }
        public final ArrowPosition getArrowPosition() { return arrowPosition == null ? ArrowPosition.TRAILING : arrowPosition.get(); }
        public final ListSpinnerCaspianSkin<T> withArrowPosition(ArrowPosition value) { setArrowPosition(value); return this; }
        public enum ArrowPosition {LEADING, TRAILING, SPLIT}
        
        /**
         * arrowDirection
         */
        public final ObjectProperty<ArrowDirection> arrowDirectionProperty() 
        {
            if (arrowDirection == null) 
            {
                arrowDirection = new StyleableObjectProperty<ArrowDirection>(ArrowDirection.HORIZONTAL) 
                {
                    @Override public void invalidated()
                    {
                        setArrowCSS();
                        layout();
                    }
                    
                    @Override public CssMetaData<ListSpinner,ArrowDirection> getCssMetaData() { return StyleableProperties.ARROW_DIRECTION; }
                    @Override public Object getBean() { return ListSpinnerCaspianSkin.this; }
                    @Override public String getName() { return "arrowDirection"; }
                };
            }
            return arrowDirection;
        }
        private ObjectProperty<ArrowDirection> arrowDirection = null;
        public final void setArrowDirection(ArrowDirection value) { arrowDirectionProperty().set(value); }
        public final ArrowDirection getArrowDirection() { return arrowDirection == null ? ArrowDirection.HORIZONTAL : arrowDirection.get(); }
        public final ListSpinnerCaspianSkin<T> withArrowDirection(ArrowDirection value) { setArrowDirection(value); return this; }
        public enum ArrowDirection {VERTICAL, HORIZONTAL}
        
        /**
         * valueAlignment
         */
        public final ObjectProperty<Pos> valueAlignmentProperty() 
        {
            if (valueAlignment == null) 
            {
                valueAlignment = new StyleableObjectProperty<Pos>(Pos.CENTER_LEFT) 
                {
                    @Override public void invalidated()
                    {
                        alignValue();
                    }
                    
                    @Override public CssMetaData<ListSpinner,Pos> getCssMetaData() { return StyleableProperties.VALUE_ALIGNMENT; }
                    @Override public Object getBean() { return ListSpinnerCaspianSkin.this; }
                    @Override public String getName() { return "valueAlignment"; }
                };
            }
            return valueAlignment;
        }
        private ObjectProperty<Pos> valueAlignment = null;
        public final void setValueAlignment(Pos value) { valueAlignmentProperty().set(value); }
        public final Pos getValueAlignment() { return valueAlignment == null ? Pos.CENTER_LEFT : valueAlignment.get(); }
        public final ListSpinnerCaspianSkin<T> withValueAlignment(Pos value) { setValueAlignment(value); return this; }
    
        // -------------------------
            
        private static class StyleableProperties 
        {
            private static final CssMetaData<ListSpinner, ArrowPosition> ARROW_POSITION = new CssMetaData<ListSpinner, ArrowPosition>("-fxx-arrow-position", new EnumConverter<ArrowPosition>(ArrowPosition.class), ArrowPosition.TRAILING ) 
            {
                @Override public boolean isSettable(ListSpinner n) { return !((ListSpinnerCaspianSkin)n.getSkin()).arrowPositionProperty().isBound(); }
                @Override public StyleableProperty<ArrowPosition> getStyleableProperty(ListSpinner n) { return (StyleableProperty<ArrowPosition>)((ListSpinnerCaspianSkin)n.getSkin()).arrowPositionProperty(); }
            };
            
            private static final CssMetaData<ListSpinner, ArrowDirection> ARROW_DIRECTION = new CssMetaData<ListSpinner, ArrowDirection>("-fxx-arrow-direction", new EnumConverter<ArrowDirection>(ArrowDirection.class), ArrowDirection.HORIZONTAL ) 
            {
                @Override public boolean isSettable(ListSpinner n) { return !((ListSpinnerCaspianSkin)n.getSkin()).arrowDirectionProperty().isBound(); }
                @Override public StyleableProperty<ArrowDirection> getStyleableProperty(ListSpinner n) { return (StyleableProperty<ArrowDirection>) ((ListSpinnerCaspianSkin)n.getSkin()).arrowDirectionProperty(); }
            };
            
            private static final CssMetaData<ListSpinner, Pos> VALUE_ALIGNMENT = new CssMetaData<ListSpinner, Pos>("-fxx-value-alignment", new EnumConverter<Pos>(Pos.class), Pos.CENTER_LEFT ) 
            {
                @Override public boolean isSettable(ListSpinner n) { return !((ListSpinnerCaspianSkin)n.getSkin()).valueAlignmentProperty().isBound(); }
                @Override public StyleableProperty<Pos> getStyleableProperty(ListSpinner n) { return (StyleableProperty<Pos>) ((ListSpinnerCaspianSkin)n.getSkin()).valueAlignmentProperty(); }
            };
            
            private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
            static 
            {
                final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<CssMetaData<? extends Styleable, ?>>(SkinBase.getClassCssMetaData());
                styleables.add(ARROW_POSITION);
                styleables.add(ARROW_DIRECTION);
                styleables.add(VALUE_ALIGNMENT);
                STYLEABLES = Collections.unmodifiableList(styleables);                
            }
        }
        
        /** 
         * @return The CssMetaData associated with this class, which may include the
         * CssMetaData of its super classes.
         */    
        public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() 
        {
            return StyleableProperties.STYLEABLES;
        }

        /**
         * This method should delegate to {@link Node#getClassCssMetaData()} so that
         * a Node's CssMetaData can be accessed without the need for reflection.
         * @return The CssMetaData associated with this node, which may include the
         * CssMetaData of its super classes.
         */
        public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() 
        {
            return getClassCssMetaData();
        }
        
	// ==================================================================================================================
	// DRAW
	
	/**
	 * Construct the nodes. 
	 * Spinner uses a GridPane where the arrows and the node for the value are laid out according to the arrows direction and location.
	 * A place holder in inserted into the GridPane to hold the value node, so the spinner can alternate between editable or readonly mode, without having to recreate the GridPane.  
	 */
	private void createNodes()
	{
		// left arrow
		decrementArrow = new Region();
		decrementArrow.getStyleClass().add("idle");

		// place holder for showing the value
		valueHolderNode = new BorderPane();
		valueHolderNode.getStyleClass().add("valuePane");
		//valueHolderNode.setStyle("-fx-border-color: white;");
		
		// right arrow
		incrementArrow = new Region();
		incrementArrow.getStyleClass().add("idle");

		// construct a placeholder node
		skinNode = new BorderPane();
		skinNode.setCenter(valueHolderNode);

		// we're not catching the mouse events on the individual children, but let it bubble up to the parent and handle it there, this makes our life much more simple
		// process mouse clicks
		skinNode.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override public void handle(MouseEvent evt)
			{
			// if click was the in the greater vicinity of the decrement arrow
			if (mouseEventOverArrow(evt, decrementArrow))
			{
				// left
				unclickArrows();
				decrementArrow.getStyleClass().add("clicked");
				getSkinnable().decrement();
				unclickTimer.restart();
				return;
			}

			// if click was the in the greater vicinity of the increment arrow
			if (mouseEventOverArrow(evt, incrementArrow))
			{
				// right
				unclickArrows();
				incrementArrow.getStyleClass().add("clicked");
				getSkinnable().increment();
				unclickTimer.restart();
				return;
			}
			}
		});
		// process mouse holds
		skinNode.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override public void handle(MouseEvent evt)
			{
			// if click was the in the greater vicinity of the decrement arrow
			if (mouseEventOverArrow(evt, decrementArrow))
			{
				// left
				decrementArrow.getStyleClass().add("clicked");
				repeatDecrementClickTimer.restart();
				return;
			}

			// if click was the in the greater vicinity of the increment arrow
			if (mouseEventOverArrow(evt, incrementArrow))
			{
				// right
				incrementArrow.getStyleClass().add("clicked");
				repeatIncrementClickTimer.restart();
				return;
			}
			}
		});
		skinNode.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			@Override public void handle(MouseEvent evt)
			{
			unclickArrows();
			repeatDecrementClickTimer.stop();
			repeatIncrementClickTimer.stop();
			}
		});
		skinNode.setOnMouseExited(new EventHandler<MouseEvent>()
		{
			@Override public void handle(MouseEvent evt)
			{
			unclickArrows();
			repeatDecrementClickTimer.stop();
			repeatIncrementClickTimer.stop();
			}
		});
		// mouse wheel
		skinNode.setOnScroll(new EventHandler<ScrollEvent>()
		{
			@Override
			public void handle(ScrollEvent evt)
			{
			// if click was the in the greater vicinity of the decrement arrow
			if (evt.getDeltaY() < 0 || evt.getDeltaX() < 0)
			{
				// left
				unclickArrows();
				decrementArrow.getStyleClass().add("clicked");
				getSkinnable().decrement();
				unclickTimer.restart();
				return;
			}

			// if click was the in the greater vicinity of the increment arrow
			if (evt.getDeltaY() > 0 || evt.getDeltaX() > 0)
			{
				// right
				unclickArrows();
				incrementArrow.getStyleClass().add("clicked");
				getSkinnable().increment();
				unclickTimer.restart();
				return;
			}
			}
		});
		
		// add to self
		getSkinnable().getStyleClass().add(this.getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control
		getChildren().add(skinNode);
	}
	private Region decrementArrow = null;
	private Region incrementArrow = null;
	private BorderPane skinNode = null;
	private BorderPane valueHolderNode;
	
	// timer to remove the click styling on the arrows after a certain delay
	final private Timer unclickTimer = new Timer(new Runnable()
	{
		@Override
		public void run()
		{
		unclickArrows();
		}
	}).withDelay(Duration.millis(100)).withRepeats(false);

	// timer to handle the holding of the decrement button
	final private Timer repeatDecrementClickTimer = new Timer(new Runnable()
	{
		@Override
		public void run()
		{
		getSkinnable().decrement();
		}
	}).withDelay(Duration.millis(500)).withCycleDuration(Duration.millis(50));
	
	// timer to handle the holding of the increment button
	final private Timer repeatIncrementClickTimer = new Timer(new Runnable()
	{
		@Override
		public void run()
		{
		getSkinnable().increment();
		}
	}).withDelay(Duration.millis(500)).withCycleDuration(Duration.millis(50));

	/**
	 * Check if the mouse event is considered to have happened over the arrow
	 * @param evt
	 * @param region
	 * @return
	 */
	private boolean mouseEventOverArrow(MouseEvent evt, Region region)
	{
		// if click was the in the greater vicinity of the decrement arrow
		Point2D lClickInRelationToArrow = region.sceneToLocal(evt.getSceneX(), evt.getSceneY());
		if ( lClickInRelationToArrow.getX() >= 0.0 && lClickInRelationToArrow.getX() <= region.getWidth()
		  && lClickInRelationToArrow.getY() >= 0.0 && lClickInRelationToArrow.getY() <= region.getHeight()
		   )
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Remove clicked CSS styling from the arrows
	 */
	private void unclickArrows()
	{
		decrementArrow.getStyleClass().remove("clicked");
		incrementArrow.getStyleClass().remove("clicked");
	}
	
	/**
	 * Put the correct node for the value's place holder: 
	 * - either the TextField when in editable mode, 
	 * - or a node generated by the cell factory when in readonly mode.  
	 */
	private void replaceValueNode()
	{
		// clear
		valueHolderNode.getChildren().clear();
		
		// if not editable
		if (getSkinnable().isEditable() == false)
		{
			// use the cell factory
			Node lNode = getSkinnable().getCellFactory().call(getSkinnable());
			//lNode.setStyle("-fx-border-color: blue;");
			valueHolderNode.setCenter(lNode);
			if (lNode.getStyleClass().contains("value") == false) lNode.getStyleClass().add("value");
			if (lNode.getStyleClass().contains("readonly") == false) lNode.getStyleClass().add("readonly");
		}
		else
		{
			// use the textfield
			if (textField == null) 
			{
				textField = new TextField();
				textField.getStyleClass().add("value");
				textField.getStyleClass().add("editable");
				
				// process text entry
				textField.focusedProperty().addListener(new InvalidationListener()
				{			
					@Override
					public void invalidated(Observable arg0)
					{
						if (textField.isFocused() == false) 
						{
							parse(textField);
						}
					}
				});
				textField.setOnAction(new EventHandler<ActionEvent>()
				{
					@Override
					public void handle(ActionEvent evt)
					{
						parse(textField);
					}
				});
				textField.setOnKeyPressed(new EventHandler<KeyEvent>() 
				{
		            @Override public void handle(KeyEvent t) 
		            {
		                if (t.getCode() == KeyCode.ESCAPE) 
		                {
		    				// refresh
		    				refreshValue();
		                }
		            }
		        });
				
				// alignment
				textField.alignmentProperty().bind(valueAlignmentProperty());
			}
			valueHolderNode.setCenter(textField);
			//textField.setStyle("-fx-border-color: blue;");
		}
		
		// align
		alignValue();
	}
	private TextField textField = null;

	/**
	 * align the value inside the plave holder
	 */
	private void alignValue()
	{
		// valueHolderNode always only holds one child (the value)
		BorderPane.setAlignment(valueHolderNode.getChildren().get(0), valueAlignmentProperty().getValue());
	}
	
	// ==================================================================================================================
	// EDITABLE
	
	/**
	 * Parse the contents of the textfield
	 * @param textField
	 */
	protected void parse(TextField textField)
	{
		// get the text to parse
		String lText = textField.getText();

		// process it
		listSpinnerBehavior.parse(lText);
		
		// refresh
		refreshValue();
		return;
	}
	
	/**
	 * Lays out the spinner, depending on the location and direction of the arrows.
	 */
	private void layout()
	{
		// get the things we decide on
		ArrowDirection lArrowDirection = getArrowDirection();
		ArrowPosition lArrowPosition = getArrowPosition();
		
		// get helper values
		ColumnConstraints lColumnValue = new ColumnConstraints(valueHolderNode.getMinWidth(), valueHolderNode.getPrefWidth(), Double.MAX_VALUE);
		lColumnValue.setHgrow(Priority.ALWAYS);
		ColumnConstraints lColumnArrow = new ColumnConstraints(10);
		
		// get helper values
		RowConstraints lRowValue = new RowConstraints(valueHolderNode.getMinHeight(), valueHolderNode.getPrefHeight(), Double.MAX_VALUE);
		lRowValue.setVgrow(Priority.ALWAYS);
		RowConstraints lRowArrow = new RowConstraints(10);

		// create the grid
		skinNode.getChildren().clear();
		skinNode.setCenter(valueHolderNode);

		if (lArrowDirection == ArrowDirection.HORIZONTAL)
		{
			if (lArrowPosition == ArrowPosition.LEADING)
			{
				HBox lHBox = new HBox(0);
				lHBox.add(decrementArrow, new HBox.C().hgrow(Priority.ALWAYS));
				lHBox.add(incrementArrow, new HBox.C().hgrow(Priority.ALWAYS));
				skinNode.setLeft(lHBox);
				BorderPane.setAlignment(lHBox, Pos.CENTER_LEFT);
				//lHBox.setStyle("-fx-border-color: blue;");
			}
			if (lArrowPosition == ArrowPosition.TRAILING)
			{
				HBox lHBox = new HBox(0);
				lHBox.add(decrementArrow, new HBox.C().hgrow(Priority.ALWAYS));
				lHBox.add(incrementArrow, new HBox.C().hgrow(Priority.ALWAYS));
				skinNode.setRight(lHBox);
				BorderPane.setAlignment(lHBox, Pos.CENTER_RIGHT);
				//lHBox.setStyle("-fx-border-color: blue;");
			}
			if (lArrowPosition == ArrowPosition.SPLIT)
			{
				skinNode.setLeft(decrementArrow);
				skinNode.setRight(incrementArrow);
				BorderPane.setAlignment(decrementArrow, Pos.CENTER_LEFT);
				BorderPane.setAlignment(incrementArrow, Pos.CENTER_RIGHT);
			}
		}
		if (lArrowDirection == ArrowDirection.VERTICAL)
		{
			if (lArrowPosition == ArrowPosition.LEADING)
			{
				VBox lVBox = new VBox(0);
				lVBox.add(incrementArrow, new VBox.C().vgrow(Priority.ALWAYS));
				lVBox.add(decrementArrow, new VBox.C().vgrow(Priority.ALWAYS));
				skinNode.setLeft(lVBox);
				BorderPane.setAlignment(lVBox, Pos.CENTER_LEFT);
				//lVBox.setStyle("-fx-border-color: blue;");
			}
			if (lArrowPosition == ArrowPosition.TRAILING)
			{
				VBox lVBox = new VBox(0);
				lVBox.add(incrementArrow, new VBox.C().vgrow(Priority.ALWAYS));
				lVBox.add(decrementArrow, new VBox.C().vgrow(Priority.ALWAYS));
				skinNode.setRight(lVBox);
				BorderPane.setAlignment(lVBox, Pos.CENTER_RIGHT);
				//lVBox.setStyle("-fx-border-color: blue;");
			}
			if (lArrowPosition == ArrowPosition.SPLIT)
			{
				skinNode.setTop(incrementArrow);
				skinNode.setBottom(decrementArrow);
				BorderPane.setAlignment(incrementArrow, Pos.TOP_CENTER);
				BorderPane.setAlignment(decrementArrow, Pos.BOTTOM_CENTER);
			}
		}
	}
	
	/**
	 * Set the CSS according to the direction of the arrows, so the correct arrows are shown
	 */
	private void setArrowCSS()
	{
		decrementArrow.getStyleClass().remove("down-arrow");
		decrementArrow.getStyleClass().remove("left-arrow");
		incrementArrow.getStyleClass().remove("up-arrow");
		incrementArrow.getStyleClass().remove("right-arrow");
		if (getArrowDirection().equals(ArrowDirection.HORIZONTAL))
		{
			decrementArrow.getStyleClass().add("left-arrow");
			incrementArrow.getStyleClass().add("right-arrow");
		}
		else
		{
			decrementArrow.getStyleClass().add("down-arrow");
			incrementArrow.getStyleClass().add("up-arrow");
		}
	}
}
