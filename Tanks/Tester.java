//Justin Tran Period 5
import java.applet.Applet;
import java.awt.Graphics;
import javax.swing.Timer;
import java.lang.Thread;
import java.lang.Math;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.sound.sampled.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;

public class Tester extends java.applet.Applet implements ActionListener, MouseListener, MouseMotionListener, KeyListener //use frames to delay the bounce count of the bullets
{
	Timer t;
	Image picture;
	int xCoord,yCoord,xClicked,yClicked,xWindow,yWindow,xTank1,yTank1,xTank2,yTank2,tank1HP,tank2HP,tankTurn,turnCount,gravityCount,power,powerCount,bounces,bouncesCount;
	double deg,gravity;
	boolean musicPlayed,firstClick;
	String degr;
	public Font font;
    Image virtualMem;
	Graphics gBuffer;
	static File file;
    static AudioInputStream stream;
    static Clip music;

	public void init() //Constructor
	{
		t = new Timer(10, this); // Overall Program Speed: Lower # --> Faster
		t.start();
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		xWindow = getWidth();
		yWindow = getHeight();
		virtualMem = createImage(xWindow,yWindow);
		gBuffer = virtualMem.getGraphics();
		xTank1 = 50; yTank1 = yWindow-15;
		xTank2 = xWindow-50; yTank2 = yWindow-15;
		tank1HP = 3; tank2HP = 3;
		tankTurn = 1;
		gravity = -0.1; power = 9; bounces = 1;
		gravityCount = 1; powerCount = 1; bouncesCount = 1;
	}

	public void paint(Graphics g)  // Main Method
	{
		gBuffer.clearRect(0,0,xWindow,yWindow);
		picture = getImage(getDocumentBase(), "PyramidBG.JPG");
		gBuffer.drawImage(picture,0,0,1584,780,this);
		drawStuff(g);
		bangBang(g);
		gBuffer.setColor(Color.DARK_GRAY);
		gBuffer.fillRect(xCoord-2,yCoord-15,4,30); //crosshair
		gBuffer.fillRect(xCoord-15,yCoord-2,30,4);
		if(xTank1 >= 767)
			xTank1 = 737;
		if(xTank2 <= 817)
			xTank2 = 847;
		gBuffer.setColor(Color.BLACK);
		gBuffer.setFont(new Font("Calbri",Font.BOLD,15));
		if(tankTurn == 1)
		{
			deg = Math.atan((double)(yTank1-yCoord)/(xCoord-xTank1));
			degr = "" + deg;
			degr = degr.substring(0,4);
			
			gBuffer.drawString("f(x,deg) = ("+(gravity/2)+"x^2"+" - "+power+"cos("+degr+")"+"*"+degr+" + C) / [cos("+degr+")]",50,190);
			gBuffer.drawString("f'(x,deg) = ("+gravity+"x"+" + "+power+"sin("+degr+")) / [cos("+degr+")]",50,220);
			gBuffer.drawString("f''(x,deg) = ("+gravity+") "+"/ [cos("+degr+")]",50,250);
		}
		if(tankTurn == 2)
		{
			deg = Math.atan((double)(yTank2-yCoord)/(xCoord-xTank2));
			degr = "" + deg;
			degr = degr.substring(0,4);
			gBuffer.drawString("f(x,deg) = -("+(gravity/2)+"x^2"+" - "+power+"cos("+degr+")"+"*"+degr+" + C) / [cos("+degr+")]",50,190);
			gBuffer.drawString("f'(x,deg) = -("+gravity+"x"+" + "+power+"sin("+degr+")) / [cos("+degr+")]",50,220);
			gBuffer.drawString("f''(x,deg) = ("+gravity+") "+"/ [cos("+degr+")]",50,250);
		}
		if(tank1HP <= 0)
		{
			gBuffer.setColor(Color.MAGENTA);
			gBuffer.setFont(new Font("Calbri",Font.BOLD,200));
			gBuffer.drawString("PINK WINS",250,400);
		}
		if(tank2HP <= 0)
		{
			gBuffer.setColor(Color.GREEN);
			gBuffer.setFont(new Font("Calbri",Font.BOLD,200));
			gBuffer.drawString("GREEN WINS",150,400);
		}
		g.drawImage (virtualMem,0,0, this);
	}
	
	public void bangBang(Graphics g)
	{	
		gBuffer.setColor(Color.BLACK);
		for(Bullet b: Bullet.bulletParams)
		{
			if((b.xBullet+8 >= xWindow || b.xBullet-8 <= 0)||(b.xBullet+8>=767&&b.xBullet-8<=779&&b.yBullet+8>=680&&b.yBullet-8<=780)|| //fix right of the vertical platform
				(b.xBullet+8>=817&&b.xBullet-8<=805&&b.yBullet+8>=680&&b.yBullet-8<=780)||
				(b.xBullet+8>=767&&b.xBullet-8<=779&&b.yBullet+8>=200&&b.yBullet-8<=400)||
			    (b.xBullet+8>=767&&b.xBullet-8<=779&&b.yBullet+8>=200&&b.yBullet-8<=400)||
				(b.xBullet+8>=592&&b.xBullet-8<=604&&b.yBullet+8>=550&&b.yBullet-8<=600)||
				(b.xBullet+8>=980&&b.xBullet-8<=992&&b.yBullet+8>=550&&b.yBullet-8<=600) && b.bounce <= bounces)
				{
					b.bSpeedX = -b.bSpeedX;
					b.bounce++;
				}
			if((b.yBullet+8 >= yWindow)||(b.xBullet+8>=592&&b.xBullet-8<=992&&b.yBullet>=538&&b.yBullet-8<=550)|| 
			   (b.xBullet+8>=767&&b.xBullet-8<=779&&b.yBullet+8>=200&&b.yBullet-8<=212)||
			   (b.xBullet+8>=767&&b.xBullet-8<=779&&b.yBullet+8>=388&&b.yBullet-8<=400)||
			   (b.xBullet+8>=767&&b.xBullet-8<=779&&b.yBullet+8>=200&&b.yBullet-8<=400)||
			   (b.xBullet+8>=767&&b.xBullet-8<=779&&b.yBullet+8>=200&&b.yBullet-8<=400)||
			   (b.xBullet+8>=805&&b.xBullet-8<=817&&b.yBullet+8>=668&&b.yBullet-8<=680) && b.bounce <= bounces)
				{
					b.frame = 0;
					b.bSpeedY = (int)((3.0/4)*b.bSpeedY);
					b.bounce++;
				}
			if(b.xBullet+8>=592&&b.xBullet-8<=992&&b.yBullet+8>=588&&b.yBullet-8<=600) //for bouncing down
			{
				b.bSpeedY = (-4/5)*b.bSpeedY;
			}
			if(b.bounce >= bounces+1) //checks if bullet hits tanks and causes an explosion
			{
				if(b.xBullet>xTank1-60&&b.xBullet<xTank1+60&&b.yBullet>yTank1-20&&b.draw==true)
					tank1HP--;
				if(b.xBullet>xTank2-60&&b.xBullet<xTank2+60&&b.yBullet>yTank2-20&&b.draw==true)
					tank2HP--;
				b.draw = false;
				turnCount++;
				picture = getImage(getDocumentBase(), "Boom.GIF");
				gBuffer.drawImage(picture,(int)b.xBullet-40,(int)b.yBullet-55,80,80,this);
				b.boomCount++;
				if(b.boomCount > 70)
				{
					Bullet.bulletParams.remove(b);
				}
			}
			
			if(b.bounce <= bounces) //moves the bullet every frame
			{
				if(b.quad == 1) // Quad I
					{b.xBullet = b.xBullet + b.bSpeedX*Math.cos(b.degrees);
					 b.yBullet = b.yBullet - (b.bSpeedY*Math.sin(b.degrees) + gravity*b.frame);}			
				if(b.quad == 2) // Quad II
					{b.xBullet = b.xBullet - b.bSpeedX*Math.cos(b.degrees);						 // f(x,deg) = (-0.05x^2 - 9cos(deg)*deg' + C) / [cos(deg)]
					 b.yBullet = b.yBullet + (b.bSpeedY*Math.sin(b.degrees) - gravity*b.frame);} //f'(x,deg) = (-0.1x + 9sin(deg)) / [cos(deg)]
				b.frame++;																		 //f"(x,deg) = (-0.1) / [cos(deg)]
			}

			if((b.xBullet > 0 && b.xBullet < xWindow && b.yBullet > 0 && b.yBullet < yWindow) && b.draw)
				gBuffer.fillOval((int)b.xBullet-8,(int)b.yBullet-8,16,16);
		}			
	}
	
	public void drawStuff(Graphics g)
	{
		switch(tankTurn)
		{
			case 1: gBuffer.setColor(Color.GREEN); gBuffer.fillRect(50,50,50,50); break;
			case 2: gBuffer.setColor(Color.MAGENTA); gBuffer.fillRect(1484,50,50,50); break; 
		}
		//draws tank1
		gBuffer.setColor(Color.GREEN);
		Polygon p1 = new Polygon();
		p1.addPoint(xTank1-20,yTank1);
		p1.addPoint(xTank1-30,yTank1);
		p1.addPoint(xTank1-20,yTank1+20);
		p1.addPoint(xTank1,yTank1);
		gBuffer.fillPolygon(p1);
		Polygon p2 = new Polygon();
		p2.addPoint(xTank1+20,yTank1);
		p2.addPoint(xTank1+30,yTank1);
		p2.addPoint(xTank1+20,yTank1+20);
		p2.addPoint(xTank1+20,yTank1);
		gBuffer.fillPolygon(p2);
		gBuffer.fillRect(xTank1-20,yTank1,40,15);
		gBuffer.fillOval(xTank1-12,yTank1-12,24,24);
		gBuffer.fillRect(xTank1-20,yTank1-18,(int)((double)tank1HP/3*40),4);
		//draws tank2
		gBuffer.setColor(Color.MAGENTA);
		Polygon p3 = new Polygon();
		p3.addPoint(xTank2-20,yTank2);
		p3.addPoint(xTank2-30,yTank2);
		p3.addPoint(xTank2-20,yTank2+20);
		p3.addPoint(xTank2,yTank2);
		gBuffer.fillPolygon(p3);
		Polygon p4 = new Polygon();
		p4.addPoint(xTank2+20,yTank2);
		p4.addPoint(xTank2+30,yTank2);
		p4.addPoint(xTank2+20,yTank2+20);
		p4.addPoint(xTank2+20,yTank2);
		gBuffer.fillPolygon(p4);
		gBuffer.fillRect(xTank2-20,yTank2,40,15);
		gBuffer.fillOval(xTank2-12,yTank2-12,24,24);
		gBuffer.fillRect(xTank2-20,yTank2-18,(int)((double)tank2HP/3*40),4);
		//draws borders and platforms
		gBuffer.setColor(Color.BLACK);
		gBuffer.drawLine(0,779,1584,779); //borders
		gBuffer.drawLine(0,1,1584,1);
		gBuffer.drawLine(0,1,0,779);
		gBuffer.drawLine(1583,1,1583,779);
			gBuffer.fillRect(592,550,400,50); //horizontal platform
			gBuffer.fillRect(767,200,50,200); //vertical platform upper
			gBuffer.fillRect(767,680,50,100);  //vertical platform lower
		gBuffer.fillRect(722,50,140,100); //bounce display
		gBuffer.fillRect(722,23,140,25);
		gBuffer.fillRect(552,50,150,100); //power display
		gBuffer.fillRect(552,23,150,25);
		gBuffer.fillRect(882,50,210,100); //gravity display
		gBuffer.fillRect(882,23,210,25);
		
		gBuffer.setColor(Color.WHITE);
		gBuffer.setFont(new Font("Calbri",Font.BOLD,100));// Center display
		gBuffer.drawString("" + bounces,765,135);
		gBuffer.setFont(new Font("Calbri",Font.BOLD,20));
		gBuffer.drawString("Bounces [B]",738,42);
		
		gBuffer.setFont(new Font("Calbri",Font.BOLD,100));// Left display
		gBuffer.drawString("" + power,575,135);
		gBuffer.setFont(new Font("Calbri",Font.BOLD,20));
		gBuffer.drawString("Power [P]",590,42);
		
		gBuffer.setFont(new Font("Calbri",Font.BOLD,100));// Right display
		gBuffer.drawString("" + gravity,905,135);
		gBuffer.setFont(new Font("Calbri",Font.BOLD,20));
		gBuffer.drawString("Gravity [G]",940,42);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		repaint();
	}
	
	public void update(Graphics g)
	{
		paint(g);
	}
	
	public void keyTyped(KeyEvent e){} //KeyListener Methods
	public void keyReleased(KeyEvent e)
	{		 
	}
	public void keyPressed(KeyEvent e)
	{
		int keyPressed = e.getKeyCode();
		switch(tankTurn)
		{
			case 1:
			{
				if(keyPressed==KeyEvent.VK_W)
				{
					deg = Math.atan((double)(yTank1-yCoord)/(xCoord-xTank1));
					Bullet bangBang = new Bullet(xTank1,yTank1,power,power,deg,0,0,0,0,true);
					
					if(xCoord > xTank1 && yCoord < yTank1) // Quad I
						{bangBang.quad = 1;}			
					if(xCoord < xTank1 && yCoord < yTank1) // Quad II
						{bangBang.quad = 2;}			
					if(xCoord < xTank1 && yCoord > yTank1) // Quad III
						{bangBang.quad = 3;}	
					if(xCoord > xTank1 && yCoord > yTank1) // Quad IV
						{bangBang.quad = 4;}
					////////////////////////////////////////////////////
					if(xCoord > xTank1 && yCoord == yTank1) // RIGHT
						{bangBang.quad = 1;} 
					if(xCoord < xTank1 && yCoord == yTank1) // LEFT 
						{bangBang.quad = 3;}
					if(xCoord == xTank1 && yCoord > yTank1) // BELOW
						{bangBang.quad = 5;}
					if(xCoord == xTank1 && yCoord < yTank1) // ABOVE
						{bangBang.quad = 6;}
					if(xCoord == xTank1 && yCoord == yTank1) // Exactly at (xCoord,yCoord)
						{bangBang.quad = 1;}
						
					Bullet.bulletParams.add(bangBang);
					tankTurn = 2;
				}
				if(keyPressed==KeyEvent.VK_A)
				{
					xTank1 -= 10;
				}
				if(keyPressed==KeyEvent.VK_D)
				{
					xTank1 += 10;
				}
				
			}
			break;
			case 2:
			{
				if(keyPressed==KeyEvent.VK_UP)
				{
					deg = Math.atan((double)(yTank2-yCoord)/(xCoord-xTank2));
					Bullet bangBang = new Bullet(xTank2,yTank2,power,power,deg,0,0,0,0,true);
					
					if(xCoord > xTank2 && yCoord < yTank2) // Quad I
						{bangBang.quad = 1;}			
					if(xCoord < xTank2 && yCoord < yTank2) // Quad II
						{bangBang.quad = 2;}			
					if(xCoord < xTank2 && yCoord > yTank2) // Quad III
						{bangBang.quad = 3;}	
					if(xCoord > xTank2 && yCoord > yTank2) // Quad IV
						{bangBang.quad = 4;}
					////////////////////////////////////////////////////
					if(xCoord > xTank2 && yCoord == yTank2) // RIGHT
						{bangBang.quad = 1;} 
					if(xCoord < xTank2 && yCoord == yTank2) // LEFT 
						{bangBang.quad = 3;}
					if(xCoord == xTank2 && yCoord > yTank2) // BELOW
						{bangBang.quad = 5;}
					if(xCoord == xTank2 && yCoord < yTank2) // ABOVE
						{bangBang.quad = 6;}
					if(xCoord == xTank2 && yCoord == yTank2) // Exactly at (xCoord,yCoord)
						{bangBang.quad = 1;}
						
					Bullet.bulletParams.add(bangBang);
					tankTurn = 1;
				}
				if(keyPressed==KeyEvent.VK_LEFT)
				{
					xTank2 -= 10;
				}
				if(keyPressed==KeyEvent.VK_RIGHT)
				{
					xTank2 += 10;;
				}
			}
			break;
		}
		if(keyPressed==KeyEvent.VK_G)
		{
			switch(gravityCount)
			{
				case 1: {gravity = -0.1;} break;
				case 2: {gravity = -0.2;} break;
				case 3: {gravity = 0;} break;
			}
			if(gravityCount == 3)
				gravityCount = 0;
			gravityCount++;
		}
		if(keyPressed==KeyEvent.VK_P)
		{
			switch(powerCount)
			{
				case 1: {power = 9;} break;
				case 2: {power = 12;} break;
				case 3: {power = 6;} break;
			}
			if(powerCount == 3)
				powerCount = 0;
			powerCount++;
		}
		if(keyPressed==KeyEvent.VK_B)
		{
			switch(bouncesCount)
			{
				case 1: {bounces = 1;} break;
				case 2: {bounces = 2;} break;
				case 3: {bounces = 3;} break;
				case 4: {bounces = 4;} break;
			}
			if(bouncesCount == 4)
				bouncesCount = 0;
			bouncesCount++;
		}
		if(keyPressed==KeyEvent.VK_DELETE)
		{
			tank1HP = 3;
			tank2HP = 3;
			xTank1 = 50; yTank2 = yWindow-15;
			xTank2 = xWindow-50; yTank2 = yWindow-15;
		}
	}
	public void mouseClicked(MouseEvent e){} //MouseListener Methods
	public void mouseEntered(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseDragged(MouseEvent e) {}
	public void mouseReleased(MouseEvent e)
	{
		xClicked = e.getX();
		yClicked = e.getY();
		if(firstClick)
		{
			switch(tankTurn)
			{
				case 1:
				{
					deg = Math.atan((double)(yTank1-yCoord)/(xCoord-xTank1));
					Bullet bangBang = new Bullet(xTank1,yTank1,power,power,deg,0,0,0,0,true);
					
					if(xCoord > xTank1 && yCoord < yTank1) // Quad I
						{bangBang.quad = 1;}			
					if(xCoord < xTank1 && yCoord < yTank1) // Quad II
						{bangBang.quad = 2;}			
					if(xCoord < xTank1 && yCoord > yTank1) // Quad III
						{bangBang.quad = 3;}	
					if(xCoord > xTank1 && yCoord > yTank1) // Quad IV
						{bangBang.quad = 4;}
					////////////////////////////////////////////////////
					if(xCoord > xTank1 && yCoord == yTank1) // RIGHT
						{bangBang.quad = 1;} 
					if(xCoord < xTank1 && yCoord == yTank1) // LEFT 
						{bangBang.quad = 3;}
					if(xCoord == xTank1 && yCoord > yTank1) // BELOW
						{bangBang.quad = 5;}
					if(xCoord == xTank1 && yCoord < yTank1) // ABOVE
						{bangBang.quad = 6;}
					if(xCoord == xTank1 && yCoord == yTank1) // Exactly at (xCoord,yCoord)
						{bangBang.quad = 1;}
						
					Bullet.bulletParams.add(bangBang);
					tankTurn = 2;	
				}
				break;
				case 2:
				{
					deg = Math.atan((double)(yTank2-yCoord)/(xCoord-xTank2));
					Bullet bangBang = new Bullet(xTank2,yTank2,power,power,deg,0,0,0,0,true);
					
					if(xCoord > xTank2 && yCoord < yTank2) // Quad I
						{bangBang.quad = 1;}			
					if(xCoord < xTank2 && yCoord < yTank2) // Quad II
						{bangBang.quad = 2;}			
					if(xCoord < xTank2 && yCoord > yTank2) // Quad III
						{bangBang.quad = 3;}	
					if(xCoord > xTank2 && yCoord > yTank2) // Quad IV
						{bangBang.quad = 4;}
					////////////////////////////////////////////////////
					if(xCoord > xTank2 && yCoord == yTank2) // RIGHT
						{bangBang.quad = 1;} 
					if(xCoord < xTank2 && yCoord == yTank2) // LEFT 
						{bangBang.quad = 3;}
					if(xCoord == xTank2 && yCoord > yTank2) // BELOW
						{bangBang.quad = 5;}
					if(xCoord == xTank2 && yCoord < yTank2) // ABOVE
						{bangBang.quad = 6;}
					if(xCoord == xTank2 && yCoord == yTank2) // Exactly at (xCoord,yCoord)
						{bangBang.quad = 1;}
						
					Bullet.bulletParams.add(bangBang);
					tankTurn = 1;
				}
				break;
			}
		}
		else
			firstClick = true;
	}
	public void mouseMoved(MouseEvent e)
	{
		xCoord = e.getX();
		yCoord = e.getY();
	}
}

class Bullet
{
	public double xBullet;
	public double yBullet;
	public double degrees;
	int quad, frame, bSpeedX, bSpeedY, bounce, boomCount;
	boolean draw;
	
	public static ArrayList <Bullet> bulletParams = new ArrayList <Bullet> ();
	
	public Bullet(double x, double y, int bx, int by, double d, int q, int b, int bc, int fr, boolean dr)
	{
		quad = q;
		degrees = d;
		bSpeedX = bx;
		bSpeedY = by;
		xBullet = x;
		yBullet = y;
		bounce = b;
		boomCount = bc;
		frame = fr;
		draw = dr;
	}
}