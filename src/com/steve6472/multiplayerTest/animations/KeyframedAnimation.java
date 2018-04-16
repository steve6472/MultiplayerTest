/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 15. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.animations;

import com.steve6472.multiplayerTest.HelperDataLayer;
import com.steve6472.sge.main.SGArray;
import com.steve6472.sge.main.Util;

public abstract class KeyframedAnimation extends Animation
{
	SGArray<KeyFrame> keyFrames;
	long currentKeyTimestamp = 0;
	long nextKeyTimestamp = 0;
	int keyFrame = -1;
	boolean remove = false;
	
	int lastId = 0;
	int currentId = 0;

	long totalTime = 0;
	
	float x, y, z, r, b, g, a, sx, sy, sz, ang, rx, ry, rz;
	float ox, oy, oz, or, ob, og, oa, osx, osy, osz, oang, orx, ory, orz;
	float px, py, pz, pr, pb, pg, pa, psx, psy, psz, pang, prx, pry, prz;
	
	public KeyframedAnimation()
	{
		keyFrames = new SGArray<KeyFrame>(0, true, false);
		setKeyFrames();
	}

	@Override
	public void render()
	{
		if (keyFrame == -1)
		{
			keyFrame++;
			nextKeyTimestamp = keyFrames.getObject(keyFrame + 1).time;
			KeyFrame kf = keyFrames.getObject(keyFrame);

			// XYZ
			
			ox = kf.getXyz().getObject(0);
			oy = kf.getXyz().getObject(1);
			oz = kf.getXyz().getObject(2);
			
			x = ox;
			y = oy;
			z = oz;
			
			// COLOR
			
			or = kf.getColor().getObject(0);
			og = kf.getColor().getObject(1);
			ob = kf.getColor().getObject(2);
			oa = kf.getColor().getObject(3);
			
			r = or;
			g = og;
			b = ob;
			a = oa;

			// SCALE
			
			osx = kf.getScalexyz().getObject(0);
			osy = kf.getScalexyz().getObject(1);
			osz = kf.getScalexyz().getObject(2);
			
			sx = osx;
			sy = osy;
			sz = osz;
			
			// ROTATION

			oang = kf.getRotxyz().getObject(0);
			orx = kf.getRotxyz().getObject(1);
			ory = kf.getRotxyz().getObject(2);
			orz = kf.getRotxyz().getObject(3);

			/*
			 * I don't know why but if this is not here it will do the first
			 * rotation in a weird way
			 */
			if (oang == 0 && orx == 0 && ory == 0 && orz == 0)
			{
				orz = 1;
			}
			
			ang = oang;
			rx = orx;
			ry = ory;
			rz = orz;
			
			for (KeyFrame k : keyFrames)
			{
				System.out.println("---------------------------------------");
				k.getColor().printContent();
				System.out.println("###");
				k.getXyz().printContent();
				System.out.println("\n---------------------------------------");
			}
		}
		
		KeyFrame keyFrame = keyFrames.getObject(this.keyFrame);
		KeyFrame nextKeyFrame = keyFrames.getObject(this.keyFrame + 1);
		
		this.currentId = keyFrame.id;
		
		float t1 = time - currentKeyTimestamp;
		float t2 = nextKeyTimestamp - currentKeyTimestamp;
		
		Mode mode = null;
		
		for (int i = 0; i < nextKeyFrame.getActs().getSize(); i++)
		{
			int action = nextKeyFrame.getActs().getObject(i);
			
//			System.out.println("Action:" + action + " Time:" + time + " Id: " + currentId);
			
			mode = nextKeyFrame.modes.getObject(i);
			
			if (mode == null)
			{
				System.err.println("Unknow mode for action #" + action + " at frame #" + this.keyFrame);
				continue;
			}
			
			switch (action)
			{
			case 0:
				if (mode == Mode.SET)
				{
					x = Util.calculateValue(t1, t2, ox, nextKeyFrame.getXyz().getObject(0));
					y = Util.calculateValue(t1, t2, oy, nextKeyFrame.getXyz().getObject(1));
					z = Util.calculateValue(t1, t2, oz, nextKeyFrame.getXyz().getObject(2));
				} else if (mode == Mode.ADD)
				{
					px = Util.calculateValue(t1, t2, 0, nextKeyFrame.getXyz().getObject(0));
					py = Util.calculateValue(t1, t2, 0, nextKeyFrame.getXyz().getObject(1));
					pz = Util.calculateValue(t1, t2, 0, nextKeyFrame.getXyz().getObject(2));
				} else if (mode == Mode.BRAZIER_ADD)
				{
					px = Util.bezierCurve(x, nextKeyFrame.bezierX, nextKeyFrame.getXyz().getObject(0), t1, t2);
					py = Util.bezierCurve(y, nextKeyFrame.bezierY, nextKeyFrame.getXyz().getObject(1), t1, t2);
					pz = Util.bezierCurve(z, nextKeyFrame.bezierZ, nextKeyFrame.getXyz().getObject(2), t1, t2);
				} else if (mode == Mode.BRAZIER_SET)
				{
					x = Util.bezierCurve(ox, nextKeyFrame.bezierX, nextKeyFrame.getXyz().getObject(0), t1, t2);
					y = Util.bezierCurve(oy, nextKeyFrame.bezierY, nextKeyFrame.getXyz().getObject(1), t1, t2);
					z = Util.bezierCurve(oz, nextKeyFrame.bezierZ, nextKeyFrame.getXyz().getObject(2), t1, t2);
				}
				break;
			case 1:
				if (mode == Mode.SET)
				{
					sx = Util.calculateValue(t1, t2, osx, nextKeyFrame.getScalexyz().getObject(0));
					sy = Util.calculateValue(t1, t2, osy, nextKeyFrame.getScalexyz().getObject(1));
					sz = Util.calculateValue(t1, t2, osz, nextKeyFrame.getScalexyz().getObject(2));
				} else if (mode == Mode.ADD)
				{
					psx = Util.calculateValue(t1, t2, 0, nextKeyFrame.getScalexyz().getObject(0));
					psy = Util.calculateValue(t1, t2, 0, nextKeyFrame.getScalexyz().getObject(1));
					psz = Util.calculateValue(t1, t2, 0, nextKeyFrame.getScalexyz().getObject(2));
				}
				break;
			case 2:
				if (mode == Mode.SET)
				{
					ang = Util.calculateValue(t1, t2, oang, nextKeyFrame.getRotxyz().getObject(0));
					rx = Util.calculateValue(t1, t2, orx, nextKeyFrame.getRotxyz().getObject(1));
					ry = Util.calculateValue(t1, t2, ory, nextKeyFrame.getRotxyz().getObject(2));
					rz = Util.calculateValue(t1, t2, orz, nextKeyFrame.getRotxyz().getObject(3));
				} else if (mode == Mode.ADD)
				{
					pang = Util.calculateValue(t1, t2, 0, nextKeyFrame.getRotxyz().getObject(0));
					prx = Util.calculateValue(t1, t2, 0, nextKeyFrame.getRotxyz().getObject(1));
					pry = Util.calculateValue(t1, t2, 0, nextKeyFrame.getRotxyz().getObject(2));
					prz = Util.calculateValue(t1, t2, 0, nextKeyFrame.getRotxyz().getObject(3));
				}
				break;
			case 3:
				if (mode == Mode.SET)
				{
					r = Util.calculateValue(t1, t2, or, nextKeyFrame.getColor().getObject(0));
					g = Util.calculateValue(t1, t2, og, nextKeyFrame.getColor().getObject(1));
					b = Util.calculateValue(t1, t2, ob, nextKeyFrame.getColor().getObject(2));
					a = Util.calculateValue(t1, t2, oa, nextKeyFrame.getColor().getObject(3));
				} else if (mode == Mode.ADD)
				{
					pr = Util.calculateValue(t1, t2, 0, nextKeyFrame.getColor().getObject(0));
					pg = Util.calculateValue(t1, t2, 0, nextKeyFrame.getColor().getObject(1));
					pb = Util.calculateValue(t1, t2, 0, nextKeyFrame.getColor().getObject(2));
					pa = Util.calculateValue(t1, t2, 0, nextKeyFrame.getColor().getObject(3));
				}
				break;
			}
		}
		
		render(x + px, y + py, z + pz, r + pr, g + pg, b + pb, a + pa, sx + psx, sy + psy, sz + psz, ang + pang, rx + prx, ry + pry, rz + prz);
		
		if (time == nextKeyTimestamp)
		{
			this.keyFrame++;
			currentKeyTimestamp = keyFrames.getObject(this.keyFrame).time;
			
			printCurrentData();
			printCurrentOData();
			printCurrentPData();
			
			//Adding P-Values
			
			x += px;
			y += py;
			z += pz;
			
			r += pr;
			g += pg;
			b += pb;
			a += pa;
			
			sx += psx;
			sy += psy;
			sz += psz;
			
			ang += pang;
			rx += prx;
			ry += pry;
			rz += prz;
			
			//Updating O-Values
			
			ox = x;
			oy = y;
			oz = z;
			
			or = r;
			og = g;
			ob = b;
			oa = a;
			
			osx = sx;
			osy = sy;
			osz = sz;
			
			oang = ang;
			orx = rx;
			ory = ry;
			orz = rz;
			
			//Resseting P-Values
			
			px = 0;
			py = 0;
			pz = 0;
			
			pr = 0;
			pg = 0;
			pb = 0;
			pa = 0;
			
			psx = 0;
			psy = 0;
			psz = 0;
			
			pang = 0;
			prx = 0;
			pry = 0;
			prz = 0;

			printCurrentOData();
			printCurrentData();
			
			System.out.println("SWITCH");
			
			if (keyFrames.getSize() <= this.keyFrame + 1)
			{
				remove = true;
				return;
			}

			nextKeyTimestamp = keyFrames.getObject(this.keyFrame + 1).time;
		}
	}
	
	public void printCurrentData()
	{
//		Util.printObjects("N:", x, y, z, r, g, b, a, sx, sy, sz, ang, rx, ry, rz, "Time:", time, "Id:", currentId);
		printData("N:", x, y, z, r, g, b, a, sx, sy, sz, ang, rx, ry, rz);
	}
	
	public void printCurrentPData()
	{
//		Util.printObjects("P:", px, py, pz, pr, pg, pb, pa, psx, psy, psz, pang, prx, pry, prz, "Time:", time, "Id:", currentId);
		printData("P:", px, py, pz, pr, pg, pb, pa, psx, psy, psz, pang, prx, pry, prz);
	}
	
	public void printCurrentOData()
	{
//		Util.printObjects("O:", ox, oy, oz, or, og, ob, oa, osx, osy, osz, oang, orx, ory, orz, "Time:", time, "Id:", currentId);
		printData("O:", ox, oy, oz, or, og, ob, oa, osx, osy, osz, oang, orx, ory, orz);
	}
	
	private void printData(String type, float x, float y, float z, float r, float g, float b, float a, float sx, float sy, float sz, float ang,
			float rx, float ry, float rz)
	{
		Util.printObjects(type, " X:", x, " Y:", y, " Z:", z, " R:", r, " G:", g, " B:", b, " A:", a, " SX:", sx, " SY:", sy, " SZ:", sz, " ANG:",
				ang, " RZ:", rx, " RY:", ry, " RZ:", rz, "Time:", time, "Id:", currentId);
	}

	protected abstract void render(float x, float y, float z, float r, float g, float b, float a, float sx, float sy, float sz, float ang, float rx, float ry, float rz);
	
	public abstract void setKeyFrames();

	@Override
	public boolean hasEnded()
	{
		return remove;
	}
	
	class KeyFrame extends HelperDataLayer
	{
		long time;
		int id;
		SGArray<Mode> modes;
		
		public KeyFrame(long time)
		{
			this.time = time + totalTime;
			modes = new SGArray<Mode>();
			totalTime += time;
			this.id = lastId++;
			translate(0, 0, 0);	//0
			scale(0, 0, 0);		//1
			rotate(0, 0, 0, 0);	//2
			color(0, 0, 0, 0);	//3
		}
		
		public KeyFrame translate_(float x, float y, float z)
		{
			xyz.setObject(0, x);
			xyz.setObject(1, y);
			xyz.setObject(2, z);
			acts.addObject(0);
			return this;
		}
		
		public KeyFrame scale_(float x, float y, float z)
		{
			scalexyz.setObject(0, x);
			scalexyz.setObject(1, y);
			scalexyz.setObject(2, z);
			acts.addObject(1);
			return this;
		}
		
		public KeyFrame scale_(float xyz)
		{
			scale_(xyz, xyz, xyz);
			return this;
		}
		
		public KeyFrame rotate_(float ang, float x, float y, float z)
		{
			rotxyz.setObject(0, ang);
			rotxyz.setObject(1, x);
			rotxyz.setObject(2, y);
			rotxyz.setObject(3, z);
			acts.addObject(2);
			return this;
		}
		
		float bezierX, bezierY, bezierZ;
		
		/**
		 * 
		 * Uses old Key Frame values as P0, current as P2 and Brazier Curve points as P1
		 * 
		 * @param x Middle X Point
		 * @param y Middle Y Point
		 * @param z Middle Z Point
		 * @return
		 */
		public KeyFrame addBezierCurvePoint(float x, float y, float z)
		{
			bezierX = x;
			bezierY = y;
			bezierZ = z;
			return this;
		}
		
		public KeyFrame color_(float red, float green, float blue, float alpha)
		{
			super.color(red, green, blue, alpha);
			acts.addObject(3);
			return this;
		}
		
		public void finish(Mode... modes)
		{
			for (Mode m : modes)
			{
				this.modes.addObject(m);
			}
			keyFrames.addObject(this);
		}
	}
	
	public enum Mode
	{
		ADD, SET, BRAZIER_ADD, BRAZIER_SET
	}

}
