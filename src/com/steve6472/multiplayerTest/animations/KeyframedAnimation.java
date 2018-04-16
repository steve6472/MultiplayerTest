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

			ox = kf.getXyz().getObject(0);
			oy = kf.getXyz().getObject(1);
			oz = kf.getXyz().getObject(2);
			
			x = ox;
			y = oy;
			z = oz;
			
			or = kf.getColor().getObject(0);
			og = kf.getColor().getObject(1);
			ob = kf.getColor().getObject(2);
			oa = kf.getColor().getObject(3);
			
			r = or;
			g = og;
			b = ob;
			a = oa;
			
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
		
		Mode mode = nextKeyFrame.mode;
		
		for (int i = 0; i < nextKeyFrame.getActs().getSize(); i++)
		{
			int action = nextKeyFrame.getActs().getObject(i);
			
//			System.out.println("Action:" + action + " Time:" + time + " Id: " + currentId);
			
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
					px = Util.brazierCurve(x, nextKeyFrame.brazierX, nextKeyFrame.getXyz().getObject(0), t1, t2);
					py = Util.brazierCurve(y, nextKeyFrame.brazierY, nextKeyFrame.getXyz().getObject(1), t1, t2);
					pz = Util.brazierCurve(z, nextKeyFrame.brazierZ, nextKeyFrame.getXyz().getObject(2), t1, t2);
				} else if (mode == Mode.BRAZIER_SET)
				{
					x = Util.brazierCurve(ox, nextKeyFrame.brazierX, nextKeyFrame.getXyz().getObject(0), t1, t2);
					y = Util.brazierCurve(oy, nextKeyFrame.brazierY, nextKeyFrame.getXyz().getObject(1), t1, t2);
					z = Util.brazierCurve(oz, nextKeyFrame.brazierZ, nextKeyFrame.getXyz().getObject(2), t1, t2);
				}
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				r = Util.calculateValue(t1, t2, or, nextKeyFrame.getColor().getObject(0));
				g = Util.calculateValue(t1, t2, og, nextKeyFrame.getColor().getObject(1));
				b = Util.calculateValue(t1, t2, ob, nextKeyFrame.getColor().getObject(2));
				a = Util.calculateValue(t1, t2, oa, nextKeyFrame.getColor().getObject(3));
				break;
			}
		}
		
		render(x + px, y + py, z + pz, r + pr, g + pg, b + pb, a + pa, 0, 0, 0, 0, 0, 0, 0);
		
		if (time == nextKeyTimestamp)
		{
			this.keyFrame++;
			currentKeyTimestamp = keyFrames.getObject(this.keyFrame).time;
			
			printCurrentData();
			printCurrentOData();
			printCurrentPData();
			
			x += px;
			y += py;
			z += pz;
			
			r += pr;
			g += pg;
			b += pb;
			a += pa;
			
			
			ox = x;
			oy = y;
			oz = z;
			
			or = r;
			og = g;
			ob = b;
			oa = a;
			
			
			px = 0;
			py = 0;
			pz = 0;
			
			pr = 0;
			pg = 0;
			pb = 0;
			pa = 0;

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
		Util.printObjects("N:", x, y, z, r, g, b, a, sx, sy, sz, ang, rx, ry, rz, "Time:", time, "Id:", currentId);
	}
	
	public void printCurrentPData()
	{
		Util.printObjects("P:", px, py, pz, pr, pg, pb, pa, psx, psy, psz, pang, prx, pry, prz, "Time:", time, "Id:", currentId);
	}
	
	public void printCurrentOData()
	{
		Util.printObjects("O:", ox, oy, oz, or, og, ob, oa, osx, osy, osz, oang, orx, ory, orz, "Time:", time, "Id:", currentId);
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
		Mode mode;
		
		public KeyFrame(long time)
		{
			this.time = time + totalTime;
			totalTime += time;
			this.id = lastId++;
			color(0, 0, 0, 0);
			translate(0, 0, 0);
			scale(0, 0, 0);
			rotate(0, 0, 0, 0);
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
			rotxyz.setObject(0, x);
			rotxyz.setObject(0, y);
			rotxyz.setObject(0, z);
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
		
		float brazierX, brazierY, brazierZ;
		
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
			brazierX = x;
			brazierY = y;
			brazierZ = z;
			return this;
		}
		
		public KeyFrame color_(float red, float green, float blue, float alpha)
		{
			super.color(red, green, blue, alpha);
			acts.addObject(3);
			return this;
		}
		
		public void finish(Mode mode)
		{
			this.mode = mode;
			keyFrames.addObject(this);
		}
	}
	
	public enum Mode
	{
		ADD, SET, BRAZIER_ADD, BRAZIER_SET
	}

}
