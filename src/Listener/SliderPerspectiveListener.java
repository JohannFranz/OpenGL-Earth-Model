package Listener;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import helper.Testat;

public class SliderPerspectiveListener implements ChangeListener{
	private Testat target;
	
	public SliderPerspectiveListener(Testat target)
	{
		this.target = target;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		JSlider source = (JSlider)e.getSource();
        float val = source.getValue()*2/(float)source.getMaximum() - 1.0f;
        target.updatePerspective(source.getName(), val);
	}
}
