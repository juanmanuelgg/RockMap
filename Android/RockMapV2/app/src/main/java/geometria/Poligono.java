package geometria;

/**
 * Created by USER on 08/03/2015.
 */
public class Poligono
{
    private Point[] points;

    public Poligono(Point[] points)
    {this.points=points;}

    /**
     * PNPOLY - Point Inclusion in Polygon Test
     * W. Randolph Franklin (WRF)
     * http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
     */
    public boolean contains(Point test)
    {
        int i,j;
        boolean result = false;
        for(i=0, j=points.length-1; i<points.length; j=i++)
        {
            if((points[i].y > test.y) != (points[j].y > test.y) &&
                    (test.x < (points[j].x - points[i].x) * (test.y - points[i].y) / (points[j].y-points[i].y) + points[i].x)) {
                result = !result;
            }
        }
        return result;
    }
}
