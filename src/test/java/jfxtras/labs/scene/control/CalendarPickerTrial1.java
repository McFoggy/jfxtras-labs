/**
 * CalendarPickerTrial1.java
 *
 * Copyright (c) 2011-2013, JFXtras
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

package jfxtras.labs.scene.control;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import jfxtras.labs.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.labs.scene.control.CalendarPicker.CalendarRange;

/**
 * @author Tom Eugelink
 */
public class CalendarPickerTrial1 extends Application {
	
    public static void main(String[] args) {
    	java.util.Locale.setDefault(new java.util.Locale("de")); // weeks starts on monday
        launch(args);       
    }

	@Override
	public void start(Stage stage) {

		VBox lVBox = new VBox();
		lVBox.setSpacing(25);
		
        // add a node
		final CalendarPicker lCalendarPicker = new CalendarPicker();
		lVBox.add(lCalendarPicker);
        
		// textfield
        {
			final TextField lTextField = new TextField();
			lTextField.setEditable(false);
	        lCalendarPicker.calendarProperty().addListener(new ChangeListener<Calendar>()
			{
				@Override
				public void changed(ObservableValue<? extends Calendar> CalendarProperty, Calendar oldValue, Calendar newValue)
				{
					lTextField.setText(CalendarTimePicker.quickFormatCalendar(newValue));
				}
			});
	        lVBox.getChildren().add(lTextField);
        }
        
		// textfield
        {
			final TextField lTextField = new TextField();
			lTextField.setEditable(false);
	        lCalendarPicker.calendars().addListener(new ListChangeListener<Calendar>()
	        {
				@Override
				public void onChanged(javafx.collections.ListChangeListener.Change<? extends Calendar> arg0)
				{
					lTextField.setText(CalendarPicker.quickFormatCalendar(lCalendarPicker.calendars()));
				}
	        });
	        lVBox.getChildren().add(lTextField);
        }

		// textfield
        {
			final TextField lTextField = new TextField();
			lTextField.setEditable(false);
	        lCalendarPicker.disabledCalendars().addListener(new ListChangeListener<Calendar>()
	        {
				@Override
				public void onChanged(javafx.collections.ListChangeListener.Change<? extends Calendar> arg0)
				{
					lTextField.setText(CalendarPicker.quickFormatCalendar(lCalendarPicker.disabledCalendars()));
				}
	        });
	        lVBox.getChildren().add(lTextField);
        }

		// textfield
        {
			final TextField lTextField = new TextField();
			lTextField.setEditable(false);
	        lCalendarPicker.highlightedCalendars().addListener(new ListChangeListener<Calendar>()
	        {
				@Override
				public void onChanged(javafx.collections.ListChangeListener.Change<? extends Calendar> arg0)
				{
					lTextField.setText(CalendarPicker.quickFormatCalendar(lCalendarPicker.highlightedCalendars()));
				}
	        });
	        lVBox.getChildren().add(lTextField);
        }

        // setup
		lCalendarPicker.setCalendar(new java.util.GregorianCalendar(2011, 06, 01)); // set a value
//		lCalendarPicker.setMode(CalendarPicker.Mode.RANGE);
		lCalendarPicker.setMode(CalendarPicker.Mode.MULTIPLE);
//        lCalendarPicker.setShowTime(true);
		lCalendarPicker.setAllowNull(false);

		// test disabling calendars
		lCalendarPicker.calendarRangeCallbackProperty().set(new Callback<CalendarRange, Void>() 
		{
			@Override
			public Void call(CalendarRange calendarRange) 
			{
				Calendar lCalendar = (Calendar)calendarRange.getEndCalendar().clone();
				
				// disabled
				lCalendarPicker.disabledCalendars().clear();
				lCalendar.add(Calendar.DATE, -1);
				lCalendarPicker.disabledCalendars().add((Calendar)lCalendar.clone());
				
				// highlighted
				lCalendarPicker.highlightedCalendars().clear();
				lCalendar.add(Calendar.DATE, -1);
				lCalendarPicker.highlightedCalendars().add((Calendar)lCalendar.clone());
				lCalendar.add(Calendar.DATE, -1);
				lCalendarPicker.highlightedCalendars().add((Calendar)lCalendar.clone());
				
				return null;
			}
		});
		
		// create scene
        Scene scene = new Scene(lVBox, 300, 500);

		// load custom CSS
        scene.getStylesheets().addAll(this.getClass().getResource(this.getClass().getSimpleName() + ".css").toExternalForm());
		
        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();	
    }
}
