package Listener;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Cube.Cube;
import helper.Testat;

public class SliderCoordListener implements ChangeListener {

	private Testat target;
	
	public SliderCoordListener(Testat target)
	{
		this.target = target;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
        	float val = (source.getValue()*2)/(float)source.getMaximum()-1;
            target.updateCoords(source.getName(), val);
        }
        
	}
}
