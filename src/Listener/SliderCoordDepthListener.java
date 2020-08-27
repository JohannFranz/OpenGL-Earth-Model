package Listener;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import helper.Testat;

public class SliderCoordDepthListener implements ChangeListener {
private Testat target;
	
	public SliderCoordDepthListener(Testat target)
	{
		this.target = target;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
        	float val = (source.getValue()*(-2))/(float)source.getMaximum();
            target.updateCoords(source.getName(), val);
        }
	}
}
