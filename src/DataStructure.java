import java.util.Arrays;
import java.lang.Math;




public class DataStructure implements DT {
	private Container minX;
	private Container maxX;
	private Container maxY;
	private Container minY;
	private int numOfPlanes;



	public DataStructure() //O(1)
	{
		this.maxX = null;
		this.minX = null;
		this.maxY = null;
		this.minY = null;
		this.numOfPlanes = 0 ; 
	}

	
	public void addPoint(Point point) {//O(n)
		//X
		Container newCon = new Container(point);//O(1)
		if(numOfPlanes==0) { //O(1) //the lists are always same sized.
			minX=newCon; 
			minY=newCon; 
			maxX=newCon; 
			maxY=newCon;
			numOfPlanes = numOfPlanes +1;
		}
		else{ 
			if (newCon.getData().getX() < minX.getData().getX()){
				minX.setPrevX(newCon);
				minX = newCon;
				numOfPlanes = numOfPlanes +1;
			}
			else if (newCon.getData().getX() > maxX.getData().getX()){
				maxX.setNextX(newCon);
				maxX = newCon;
				numOfPlanes = numOfPlanes +1;
			}
			else {
				Container currX = minX;
				while (currX.getData().getX()<= point.getX()&&
						currX.getNextX()!=null){//O(n)
					currX = currX.getNextX();
				}
				Container prevX = currX.getPrevX(); //saving the PREV value
				newCon.setNextX(currX); //O(1)
				newCon.setPrevX(prevX); //O(1)
				numOfPlanes = numOfPlanes +1;
			}



			//Y

			if (newCon.getData().getY() < minY.getData().getY()){
				minY.setPrevY(newCon);
				minY = newCon;

			}
			else if (newCon.getData().getY() > maxY.getData().getY()){
				maxY.setNextY(newCon);
				maxY = newCon;

			}
			else {
				Container currY = minY;
				while (currY.getData().getY()<= point.getY()&&
						currY.getNextY()!=null){//O(n)
					currY = currY.getNextY();
				}
				Container prevY = currY.getPrevY(); //saving the PREV value
				newCon.setNextY(currY); //O(1)
				newCon.setPrevY(prevY); //O(1)
			}


		}

	}



	
	public Point[] getPointsInRangeRegAxis(int min, int max, Boolean axis) {
		// TODO Auto-generated method stub
		this.updateIndex(); //O(n)
		//from left to right
		Container first= getMin(axis);
		while (first.getIntAxis(axis) < min) {
			first = first.getNext(axis);
			if (first==null|| first.getIntAxis(axis)>max) {
				return new Point[0]; //saves calculations if the range is empty
			}
		}
		int indexFirst= first.GetIndexAxis(axis); //saves the index that we will start with.

		//from right to left
		Container last= getMax(axis);
		while (last.getIntAxis(axis) > max) {
			last = last.getPrev(axis);
			if (last==null) {
				return new Point[0];
			}
		}
		int indexLast= last.GetIndexAxis(axis); //saves the index that we will end the array with.
		//building the array.
		Point [] newArray = new Point[indexLast-indexFirst +1];
		Container curr = first;
		int i = 0;
		for (;!curr.getData().equals(last.getData()); i++) {//O(n)
			//runs in the range (missing last element)
			newArray[i]= curr.getData();
			curr = curr.getNext(axis);
		}
		//adding last element
		newArray[i] = last.getData();
		return newArray;
	}



	
	public Point[] getPointsInRangeOppAxis(int min, int max, Boolean axis) {//O(n)
		// TODO Auto-generated method stub
		int lengthOfNewArray = getPointsInRangeRegAxis(min,max,axis).length;//O(n)
		Point[] newArray= new Point[lengthOfNewArray];

		Container curr = getMin(!axis);
		int indexInNewArray = 0;
		for (;(curr!=null);curr=curr.getNext(!axis)) {//runs threw all planes according to !axis order.
			if (curr.getIntAxis(axis)>=min &&curr.getIntAxis(axis)<=max ) {//check if the axis element is between min and max
				newArray[indexInNewArray] = curr.getData();
				indexInNewArray = indexInNewArray+1;
			}
		}
		return newArray;
	}



	
	public double getDensity() {//O(1)
		// TODO Auto-generated method stub
		if(numOfPlanes == 0)
			return 0;
		return (double)numOfPlanes / ((maxX.getData().getX()-minX.getData().getX())*(maxY.getData().getY()-minY.getData().getY()));
	}

	
	public void narrowRange(int min, int max, Boolean axis) { // O(A)
		// TODO Auto-generated method stub
		Container currAxis = getMin(axis); //running left to right until min
		for (;(numOfPlanes != 0)&&currAxis.getIntAxis(axis)<min;currAxis=currAxis.getNext(axis)){ //O(min)
			if (getMin(!axis).getData().equals(currAxis.getData())){//if curr is the first element
				if(numOfPlanes == 1) {//if its the only element (we will have to erase it) so we will stay with 0 planes
					DeleteLast(axis);
				}
				else {//its not the only plane
					currAxis.getNext(!axis).setPrev(null, !axis);//"erasing" the element
					SetMinAxis(currAxis.getNext(!axis),!axis);//update the Minimum
				}
			}

			else
				currAxis.getPrev(!axis).setNext(currAxis.getNext(!axis), !axis);//the element is between 2 others
			numOfPlanes=numOfPlanes-1;
		}	
		if(numOfPlanes != 0) {//updating the minimum in the given Axis. (deletes everyone before him).
			currAxis.setPrev((Container)null,axis);
			SetMinAxis(currAxis,axis);
		}


		// running in reverse (right to left) until max
		if(numOfPlanes == 1)
			SetMaxAxis(currAxis,!axis);
		Container RcurrAxis = getMax(axis); 
		for (;(numOfPlanes != 0)&&RcurrAxis.getIntAxis(axis)>max ;RcurrAxis=RcurrAxis.getPrev(axis)){//O(n - Max)
			if (getMax(!axis).getData().equals(RcurrAxis.getData())){//if Rcurr is the last element
				if(numOfPlanes == 1) {//if its the only element(we will have to erase it) so we will stay with 0 planes
					DeleteLast(axis);
				}
				else {//its not the only plane
					RcurrAxis.getPrev(!axis).setNext(null, !axis);//erase the element
					SetMaxAxis(RcurrAxis.getPrev(!axis),!axis);//update Maximum
				}
			}
			else if (getMin(!axis).getData().equals(RcurrAxis.getData())){//if Rcurr is the first element
				RcurrAxis.getNext(!axis).setPrev(null, !axis);//"erasing" the element
				SetMinAxis(RcurrAxis.getNext(!axis),!axis);//update the Minimum
			}
			else
				RcurrAxis.getPrev(!axis).setNext(RcurrAxis.getNext(!axis),!axis); //the element is between 2 others
			numOfPlanes=numOfPlanes-1;
		}
		if(numOfPlanes != 0) {//updating the maximum in the given Axis. (deletes everyone before him).
			RcurrAxis.setNext((Container)null,axis);
			SetMaxAxis(RcurrAxis,axis);
		}
	}


	
	public Boolean getLargestAxis() {//O(1)
		// TODO Auto-generated method stub
		if(numOfPlanes==0)
			throw new IllegalStateException("there are currently no planes in the system");
		int rangeX = maxX.getData().getX() - minX.getData().getX();
		int rangeY = maxY.getData().getY() - minY.getData().getY();
		return rangeX>rangeY;
	}

	
	public Container getMedian(Boolean axis) {//O(n/2)=O(n)
		// TODO Auto-generated method stub
		Container result = getMin(axis);
		int medPoint = (numOfPlanes /2);
		for (int i=0; i<medPoint; i++) 
			result=result.getNext(axis);//runs until the medPoint
		return result;
	}

	
	public Point[] nearestPairInStrip(Container container, double width,Boolean axis) {
		// TODO Auto-generated method stub
		if(numOfPlanes==0)
			throw new IllegalStateException("there are currently no planes in the system");

		Point[] nearestPairToReturn=new Point[2];
		double minLine= container.getIntAxis(axis)-(width/2);
		double maxLine= container.getIntAxis(axis)+(width/2);
		Container currLeft;
		Container currRight;
		int OtherAxisArrayLength =1;// to make the Array later with length B.
		double MinDistance = Distance(getMin(axis).getData(),getMax(axis).getData());//a variable to compare(will be biggest distance at this point)

		//when all planes in the Range //
		if(getMin(axis).getIntAxis(axis)>=minLine && getMax(axis).getIntAxis(axis)<=maxLine)
			return FindNearestPairAllPlanesInRange(width,axis);//O(n)


		//if only 2 points are in the system and in range
		if(numOfPlanes == 2 && getMin(axis).getIntAxis(axis)>=minLine &&getMax(axis).getIntAxis(axis)<=maxLine) {
			nearestPairToReturn[0]= getMin(axis).getData();
			nearestPairToReturn[1]=getMax(axis).getData();
			return nearestPairToReturn; //will always be only 2 points
		}
		currLeft =container;
		currRight=container;

		//running from the middle to the left.
		while(currLeft.getPrev(axis)!=null &&
				currLeft.getIntAxis(axis)>=minLine &currLeft.getPrev(axis).getIntAxis(axis)>=minLine  )  {//O(B)

			double leftDistance=Distance(currLeft.getPrev(axis).getData(),currLeft.getData());
			if(leftDistance <= MinDistance){ //if left is the smallest
				nearestPairToReturn[0]= currLeft.getPrev(axis).getData();
				nearestPairToReturn[1]= currLeft.getData();
				MinDistance = leftDistance;
				if(MinDistance == (width/2))
					return nearestPairToReturn; //for sure the smallest distance
			}
			currLeft=currLeft.getPrev(axis);
			OtherAxisArrayLength = OtherAxisArrayLength+1;

		}
		//running from middle to right
		while(currRight.getNext(axis)!=null &&
				currRight.getIntAxis(axis)<=maxLine &currRight.getNext(axis).getIntAxis(axis)<=maxLine) {
			double rightDistance=Distance(currRight.getNext(axis).getData(),currRight.getData());
			if(rightDistance <= MinDistance) { //if right is the smallest
				nearestPairToReturn[0]= currRight.getNext(axis).getData();
				nearestPairToReturn[1]= currRight.getData();
				MinDistance = rightDistance;
				if(MinDistance == (width/2))
					return nearestPairToReturn; //for sure the smallest distance
			}

			currRight=currRight.getNext(axis);
			OtherAxisArrayLength = OtherAxisArrayLength+1;
		}

		//Checking other Axis for closer Points:
		Container[] OtherAxisArray = new Container[OtherAxisArrayLength];
		Container currRightOther = container.getNext(axis);
		Container currLeftOther = container.getPrev(axis);
		OtherAxisArray[0]=container;//first element in array
		int indexInArray = 1;
		//adding Containers to the array
		while(indexInArray<OtherAxisArray.length) {//O(B
			//adding the planes in the left side of the container
			if(currLeftOther != null &&currLeftOther.getIntAxis(axis)>=minLine) {//avoid adding the same element more than 1 time
				//(if both sides are not symmetric)
				OtherAxisArray[indexInArray]=currLeftOther;
				currLeftOther=currLeftOther.getPrev(axis);
				indexInArray = indexInArray+1;

			}
			//adding the planes in the right side of the container
			if(currRightOther!=null && currRightOther.getIntAxis(axis)<=maxLine) { //avoid adding the same element more than 1 time
				//(if both sides are not symmetric)
				OtherAxisArray[indexInArray]=currRightOther;
				currRightOther=currRightOther.getNext(axis);
				indexInArray = indexInArray+1;

			}
		}
		//now the array is ready and we will merge sort it according to !axis.
		OtherAxisArray =mergeSort(OtherAxisArray,!axis); //O(BlogB)
		//now we will compare every 2 points(following !axis points) in the array to find the smallest pair:
		for(int i=0; i<OtherAxisArray.length-1; i++) {

			if(Distance(OtherAxisArray[i].getData(),OtherAxisArray[i+1].getData())<=MinDistance) {//O(B)
				nearestPairToReturn[0]=OtherAxisArray[i].getData();
				nearestPairToReturn[1]=OtherAxisArray[i+1].getData();
				MinDistance = Distance(OtherAxisArray[i].getData(),OtherAxisArray[i+1].getData());
				if(MinDistance == (width/2))
					return nearestPairToReturn; //for sure the smallest distance
			}
		}
		return nearestPairToReturn;
	}




	
	public Point[] nearestPair() {//O(n/BlogB)
		// TODO Auto-generated method stub
		Point[] nearestPairToReturn=new Point[2];
		if(numOfPlanes == 2) { //O(1)
			nearestPairToReturn[0]= getMin(true).getData();
			nearestPairToReturn[1]=getMax(true).getData();
			return nearestPairToReturn; //will always be only 2 points
		}

		if(numOfPlanes<2) { //O(1)
			return new Point[0]; //will always be only 2 points
		}
		boolean axis = getLargestAxis();//O(1)
		Container median = getMedian(axis);//O(n)
		//Calculate the nearest pairs for all the points SMALLER than the median recursively
		Point[] LeftNearestPoints = CalcLeft(median,axis);//O(n)
		//Calculate the nearest pairs for all the points LARGER than the median recursively
		Point[] RightNearestPoints = CalcRight(median,axis);//O(n)
		double DistanceLeft = Distance(LeftNearestPoints[0],LeftNearestPoints[1]);
		double DistanceRight = Distance(RightNearestPoints[0],RightNearestPoints[1]);
		double minDist;
		if (DistanceLeft >= DistanceRight) {
			minDist=DistanceRight;
			nearestPairToReturn = RightNearestPoints;
		}
		else {
			minDist=DistanceLeft;
			nearestPairToReturn = LeftNearestPoints;

		}
		Point[] CheckInRangePoints = nearestPairInStrip(median,(minDist*2),axis); //O(n/BlogB)
		double DistanceInRange = Distance(CheckInRangePoints[0],CheckInRangePoints[1]);
		if (DistanceInRange <minDist) 
			nearestPairToReturn=CheckInRangePoints;

		return nearestPairToReturn;
	}









	//TODO: add members, methods, etc.

	///////////////////////////EXTRA METHODS/////////////////////////////

	public String toString() { //for testing ourself
		//X
		String output = "<";
		Container current = minX;
		while (current != null) {
			output = output + current.toString();
			current = current.getNextX();
			if(current != null)
				output = output + ", ";
		}
		output = output + ">";

		//Y
		String outputY = "<";
		Container currentY = minY;
		while (currentY != null) {
			outputY = outputY + currentY.toString();
			currentY = currentY.getNextY();
			if(currentY != null)
				outputY = outputY + ", ";
		}
		outputY = outputY + ">";
		return "x=" +output + "\n" +"y=" + outputY;
	}



	public void updateIndex() { //O(n)
		int counterX = 0 ;
		Container currX = minX;
		while (currX !=null) { //O(n)
			currX.indexX = counterX;
			counterX = counterX +1;
			currX=currX.getNextX();
		}
		Container currY = minY;
		int counterY = 0 ;
		while (currY !=null) { //O(n)
			currY.indexY = counterY;
			counterY = counterY +1;
			currY=currY.getNextY();
		}

	}
	public Container getMin(boolean bool) {
		if(bool)
			return minX;
		else
			return minY;
	}
	public Container getMax(boolean bool) {
		if(bool)
			return maxX;
		else
			return maxY;
	}

	public void SetMinAxis(Container con ,boolean bool){
		if(bool)
			minX=con;
		else
			minY=con;
	}

	public void SetMaxAxis(Container con ,boolean bool){
		if(bool)
			maxX=con;
		else
			maxY=con;
	}




	public void DeleteLast(boolean axis){
		SetMaxAxis(null,!axis);
		SetMaxAxis(null,axis);
		SetMinAxis(null,!axis);
		SetMinAxis(null,axis);
	}

	public double Distance(Point point1,Point point2){
		double left = (double)Math.pow((point1.getX()-point2.getX()),2);
		double right= (double)Math.pow((point1.getY()-point2.getY()),2);
		return (double) (Math.sqrt(left+right));
	}


	
	
	///////////////////////////////4.2 SPLIT METHOD////////////////////////
	/*public DataStructure[] split(int val ,Boolean axis){
		DataStructure[] DSarray = new DataStructure[2];
		DataStructure DSRight = new DataStructure();
		DataStructure DSLeft = new DataStructure();
		Container thisMax = this.getMax(axis);
		Container thisMin = this.getMin(axis);
		while(thisMax!=null && thisMax.getIntAxis(axis)>=val&&
				thisMin!=null && thisMin.getIntAxis(axis)<val) {//going left and Right

			DSRight.addPoint(thisMax.getData());//adding to new

			thisMax=thisMax.getPrev(axis);
			DSLeft.addPoint(thisMin.getData());//adding to new
			thisMin=thisMin.getNext(axis);

		}
		if(thisMax.getIntAxis(axis)<val) {//DSRight is the smaller one.
			

			//update the "this" !axis
			Container curr = this.getMax(axis);
			this.SetMaxAxis(thisMax, axis);
			thisMax.setNext(null,axis);
			while(curr!=thisMax) {

				if(curr.getPrev(!axis)==null){//if I am first in !axis
					curr.getNext(!axis).setPrev(null, !axis);
					curr = curr.getPrev(axis);
					this.SetMinAxis(curr,!axis);
				}
				else {
					curr.getPrev(!axis).setNext(curr.getNext(!axis), !axis);
					curr = curr.getPrev(axis);
				}
			}
			DSarray[0]=this;//left DS
			DSarray[1]=DSRight;//right DS
			return DSarray;
		}
		else {//DSLeft is smaler
			Container curr = this.getMin(axis);
			this.SetMinAxis(thisMin, axis);
			thisMin.setPrev(null,axis);
			while(curr!=thisMin) {

				if(curr.getNext(!axis)==null) {
					curr.getPrev(!axis).setNext(null, !axis);
					curr = curr.getNext(axis);
					this.SetMaxAxis(curr,!axis);
				}
				else {
					curr.getNext(!axis).setPrev(curr.getPrev(!axis), !axis);
					curr = curr.getNext(axis);
				}
			}

			DSarray[0]=DSLeft;//left DS
			DSarray[1]=this;//right DS
			return DSarray;
		}
	}*/



	//mergeSort///////////////

	//Same Megre-Sort as learned in class, but gets a Container array that being sorted by the
	//boolean Axis value.
	public static Container[] mergeSort(Container[] arr, boolean axis) {
		if (arr == null)
			throw new NullPointerException();
		if (arr.length < 2)
			return arr;
		else {
			int mid = arr.length / 2;
			Container[] left = new Container[mid];
			Container[] right = new Container[arr.length - mid];
			for (int i = 0; i < left.length; i++) {
				left[i] = arr[i];
			}
			for (int j = 0; j < right.length; j++) {
				right[j] = arr[mid + j];
			}
			left = mergeSort(left, axis);
			right = mergeSort(right, axis);
			return merge(left, right, axis);
		}
	}

	private static Container[] merge(Container[] left, Container[] right, boolean axis) {
		Container[] result = new Container[left.length + right.length];
		int i = 0, j = 0, k = 0;
		while (i < left.length && j < right.length) {
			if (axis) {
				if (left[i].getIntAxis(!axis) <= right[j].getIntAxis(!axis)) {
					result[k] = left[i];
					i++;
				} else {
					result[k] = right[j];
					j++;
				}
			} else {
				if (left[i].getIntAxis(axis) <= right[j].getIntAxis(axis)) {
					result[k] = left[i];
					i++;
				} else {
					result[k] = right[j];
					j++;
				}
			}
			k++;
		}
		while (i < left.length) {
			result[k] = left[i];
			i++;
			k++;
		}
		while (j < right.length) {
			result[k] = right[j];
			j++;
			k++;
		}
		return result;
	}

	//////////end of MergeSort/////////////////
	public Point[] FindNearestPairAllPlanesInRange(double width,Boolean axis){
		Point[] nearestPairToReturn=new Point[2];
		double MinDistance = Distance(getMin(axis).getData(),getMax(axis).getData());//a variable to compare(will be biggest distance at this point)

		if(numOfPlanes == 2) {
			nearestPairToReturn[0]= getMin(axis).getData();
			nearestPairToReturn[1]=getMax(axis).getData();
			return nearestPairToReturn; //will always be only 2 points
		}
		//run on axis list.
		for(Container i=getMin(axis); i.getNext(axis).getNext(axis) !=null ; i = i.getNext(axis)) {
			if(Distance(i.getData(),i.getNext(axis).getData())<=MinDistance) {//O(B)
				nearestPairToReturn[0]=i.getData();
				nearestPairToReturn[1]=i.getNext(axis).getData();
				MinDistance = Distance(i.getData(),i.getNext(axis).getData());
				if(MinDistance == (width/2))
					return nearestPairToReturn; //for sure the smallest distance
			}
		}
		//run on !axis list
		for(Container i=getMin(!axis); i.getNext(!axis) !=null ; i = i.getNext(!axis)) {
			if(Distance(i.getData(),i.getNext(!axis).getData())<=MinDistance) {//O(B)
				nearestPairToReturn[0]=i.getData();
				nearestPairToReturn[1]=i.getNext(!axis).getData();
				MinDistance = Distance(i.getData(),i.getNext(!axis).getData());
				if(MinDistance == (width/2))
					return nearestPairToReturn; //for sure the smallest distance
			}
		}
		return nearestPairToReturn;
	}

	public Point[] CalcRight(Container Start,Boolean axis){//O(n)
		double MinDistance = Distance(Start.getData(),getMax(axis).getData()); //maximum distance possible.
		Point[] nearestPairToReturn= new Point[2];
		nearestPairToReturn[0]= Start.getData();
		nearestPairToReturn[1]= Start.getNext(axis).getData();
		return CalcRightREC(Start,axis,MinDistance,nearestPairToReturn);//sends to recursion
	}
	public Point[] CalcRightREC(Container Start,Boolean axis,double minDistance,Point[] acc){
		Container currRight = Start;
		if(currRight.getNext(axis)==null) 
			return acc;

		else {
			double newMinDistance=Distance(currRight.getNext(axis).getData(),currRight.getData());
			//calculate distance between "me" and my next.
			if(minDistance < newMinDistance)
				return CalcRightREC(Start.getNext(axis),axis,minDistance,acc);
			else {
				acc[0]=currRight.getNext(axis).getData();
				acc[1]=currRight.getData();
				return CalcRightREC(Start.getNext(axis),axis,newMinDistance,acc);
			}
		}
	}


	public Point[] CalcLeft(Container Start,Boolean axis){//O(n)
		double MinDistance = Distance(Start.getData(),getMin(axis).getData()); //maximum distance possible.
		Point[] nearestPairToReturn= new Point[2];
		nearestPairToReturn[0]= Start.getData();
		nearestPairToReturn[1]= Start.getPrev(axis).getData();
		return CalcLeftREC(Start,axis,MinDistance,nearestPairToReturn);//sends to recursion
	}
	public Point[] CalcLeftREC(Container Start,Boolean axis,double minDistance,Point[] acc){
		Container currLeft = Start;
		if(currLeft.getPrev(axis)==null) 
			return acc;

		else {
			double newMinDistance=Distance(currLeft.getPrev(axis).getData(),currLeft.getData());
			//calculate distance between "me" and my next.
			if(minDistance < newMinDistance)
				return CalcLeftREC(Start.getPrev(axis),axis,minDistance,acc);
			else {
				acc[0]=currLeft.getPrev(axis).getData();
				acc[1]=currLeft.getData();
				return CalcLeftREC(Start.getPrev(axis),axis,newMinDistance,acc);
			}
		}
	}





	/////////////////Main for TESTING///////////////////////////
	public static void main(String[] args) {
		DataStructure Planes = new DataStructure();
		Point P8 = new Point(1000,0,"P40");
		Point P1 = new Point(0,100,"P41");
		Point P15 = new Point(500,-200,"P50");
		Point P5 = new Point(499,15,"P51");
		//Point P10 = new Point(100,1,"P100");
		//Point P9 = new Point(9,1,"P");
		//Planes.addPoint(P10);
		//Planes.addPoint(P9);
		Planes.addPoint(P5);
		Planes.addPoint(P1);
		Planes.addPoint(P15);
		Planes.addPoint(P8);
		Planes.updateIndex();
		//String st = Planes.toString();
		//System.out.println(st);

		//DataStructure[] st2 = Planes.split(3060,true);
		//System.out.println(Planes.Distance(new Container(P3),new Container(P7)));
		//System.out.println(Arrays.toString(st2));
		//System.out.println((Planes.getMedian(true)));

	}






}











