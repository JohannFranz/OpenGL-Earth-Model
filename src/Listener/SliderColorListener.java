package Listener;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import helper.Testat;

public class SliderColorListener implements ChangeListener {

	private Testat target;
	
	public SliderColorListener(Testat target)
	{
		this.target = target;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		JSlider source = (JSlider)e.getSource();
		float val = (source.getValue())/(float)source.getMaximum();
        target.updateColors(source.getName(), val);
	}
}
