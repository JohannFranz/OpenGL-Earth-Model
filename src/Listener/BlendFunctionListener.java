package Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import Globus.BlendFunction;
import helper.Testat;

public class BlendFunctionListener implements ActionListener{

	private Testat target;
	
	public BlendFunctionListener(Testat target) {
		this.target = target;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JComboBox cb = (JComboBox)e.getSource();
		BlendFunction function = (BlendFunction)cb.getSelectedItem();
        target.updateBlendFunction(cb.getName(), function.getFunction());
	}
	

}
