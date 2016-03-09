package org.ilite.gui.vision;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;

import dataclient.robotdata.vision.CameraFeedDatabase;

public class VisionFeedToMP4 {
	
	public static void WriteToMP4(CameraFeedDatabase db, String filepath){
		System.err.println("saving");
		int frame = 0;
		
		BufferedImage img0 = db.pullFrame(0);
		long initialTime = db.getFrameTimeLength(0);

		IMediaWriter writer = ToolFactory.makeWriter(filepath);
		writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4,
                img0.getWidth(), img0.getHeight());
		try{
			while(true){
				BufferedImage img = db.pullFrame(frame);
				BufferedImage image = new BufferedImage(img.getWidth(),
	                    img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
	            image.getGraphics().drawImage(img, 0, 0, null);
				long time = db.getFrameTimeLength(frame);
				writer.encodeVideo(0, image, time - initialTime, TimeUnit.MILLISECONDS);
				System.out.println("I WROTE A FRAME CAPPA");
				frame++;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		writer.close();
	}

}
