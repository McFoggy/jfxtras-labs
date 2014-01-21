/**
 * MapTrial1.java
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

package jfxtras.labs.map;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.labs.map.tile.TileSource;
import jfxtras.labs.map.tile.TileSourceFactory;
import jfxtras.labs.map.tile.local.LocalTileSourceFactory;

/**
 * 
 * @author Mario Schroeder
 */
public class MapTrial1 extends Application {

	public static void main(String[] args) {
		Application.launch(MapTrial1.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("MapTrial1.fxml"), null,
				new MapBuilderFactory());
		
		MapPane map = (MapPane) root.lookup("#map");
		
		TileSourceFactory<String> factory = new LocalTileSourceFactory();
		
		String rootDir = getClass().getResource("tile").getFile();
		String propTiles = System.getProperty("tiles.source");
		if(propTiles != null && !propTiles.trim().isEmpty()){
			rootDir = propTiles;
		}
		File dir = new File(rootDir, "tiles");
		TileSource tileSource = factory.create(dir.getPath());
		map.setTileSource(tileSource);
		
		map.setDisplayPositionByLatLon(52.4, 5.9);
		map.zoomProperty().set(7);

		Scene scene = new Scene(root);
		
		stage.setTitle("Map Trial 1");
		
		stage.setScene(scene);
		stage.show();
		
	}

}
