
//Don't change the class name
public class Container{
	private Point data;//Don't delete or change this field;
	private Container nextX;
	private Container prevX;
	private Container nextY;
	private Container prevY;
	public int indexX;
	public int indexY;
	//Don't delete or change this function
	public Point getData()
	{
		return data;
	}


	public Container(Point point) {
		this.data = point;
		this.nextX = null;
		this.prevY = null;
		this.nextX = null;
		this.prevY = null;
	}
	
	
	public int getIntAxis(boolean bool){
		if(bool)
			return this.getData().getX();
		else
			return this.getData().getY();
	}

	public void setPrev(Container prev,boolean bool){
		if(bool) {
			this.prevX = prev;
			if (prev !=null) {
				prev.nextX = this;
			}
		}
		else {
			this.prevY = prev;
			if (prev !=null) {
				prev.nextY = this;
			}
		}

	}

	public void setNext(Container next,boolean bool){
		if (bool) {
			this.nextX = next;
			if (next !=null) {
				next.prevX = this;
			}
		}
		else {
			this.nextY = next;
			if (next !=null) {
				next.prevY = this;
			}
		}

	}


	public Container getNext(boolean bool)
	{
		if(bool)
			return nextX;
		else
			return nextY;
	}

	public Container getPrev(boolean bool)
	{
		if(bool)
			return prevX;
		else
			return prevY;
	}

	public int GetIndexAxis(boolean bool){
		if(bool)
			return indexX;
		else
			return indexY;
	}
	
	public Container getNextX()
	{
		return nextX;
	}
	public Container getPrevX()
	{
		return prevX;
	}

	public Container getNextY()
	{
		return nextY;
	}

	public Container getPrevY()
	{
		return prevY;
	}

	public void setNextX(Container nextX){
		this.nextX = nextX;
		if (nextX !=null) {
			nextX.prevX = this;
		}

	}

	public void setPrevX(Container prevX){
		this.prevX = prevX;
		if (prevX !=null) {
			prevX.nextX = this;
		}

	}

	public void setNextY(Container nextY){
		this.nextY = nextY;
		if (nextY !=null) {
			nextY.prevY= this;}
	}

	public void setPrevY(Container prevY){
		this.prevY = prevY;
		if (prevY !=null) {
			prevY.nextY = this;
		}
	}


	public String toString() {
		return data.toString() + indexX +","+indexY;
	}



}
