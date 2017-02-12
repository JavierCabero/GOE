package goe.graphics.gui;

import goe.main.GameObject;

import java.util.List;

public class DoubleB implements GameObject {

	private List<Button> bts;
	private int index = 0;

	public DoubleB(List<Button> bts) {
		this.bts = bts;

	}

	public boolean isActive() {
		return index == 0 ? bts.get(bts.size() - 1).isActive() : bts.get(index - 1).isActive();
	}

	public int getLastIndex() {
		return index == 0 ? bts.size() - 1 : index - 1;
	}
	public void input() {
		Button b = bts.get(index);
		b.input();
		if (b.isActive()) {
			index = (index + 1) % bts.size();
		}

	}

	public void logic() {

	}

	public void render() {
		bts.get(index).render();
	}

	public void activate() {
		bts.get(index).activate();
		index = (index + 1) % bts.size();
	}

}
