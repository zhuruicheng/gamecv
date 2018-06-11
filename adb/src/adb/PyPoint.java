package adb;

public class PyPoint {
	// 点类型 0 横第一点 1 左纵1点       2 右纵一点
	int type;
	int x;
	int y;
	public PyPoint(int type,int x,int y){
		this.type = type;
		this.x = x;
		this.y = y;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	@Override
	public String toString() {
		return "PyPoint [type=" + type + ", x=" + x + ", y=" + y + "]";
	}

}
