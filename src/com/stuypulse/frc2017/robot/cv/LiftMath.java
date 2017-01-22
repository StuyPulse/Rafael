/**
 * Part 1a:
 * =======
 * Use height of left reflexite strip to determine its distance,
 * and of right reflexite strip to determine its distance. Average
 * these to find the distance to the peg base.
 *
 * Part 1b:
 * =======
 * Use those distances to calculate the skew of the plane of the
 * lift. I.e., if we (sky view) draw a line from the camera to
 * the peg's base, find the angle between that line and the
 * surface of the lift.
 *
 * Part 1c:
 * =======
 * Using the skew and the distance to the peg, we get the vector
 * from us to the peg.
 *
 * Part 2:
 * =======
 * Complete the triangle whose points are the camera, the peg base, and the
 * point on the normal of the lift MIN_PIVOT_DIST inches away from the
 * gear.
 * The bot will travel along the path of the two other sides (first to
 * reach the normal, and then to approach the peg).
 *
 * Note: we could go much more complex with this. E.g. we could calculate
 * a quadratic path from us to the peg base and follow that. We would need
 * to (1) calculate the path, and (2) have a routine to follow the path.
 * This is very difficult, potentially less reliable, tho more precise. Overkill.
 *
 * NOTES:
 * ======
 * We use cylindrical coordinates (r, theta, z) for (distance from camera to thing,
 * angle from heading to thing's angle [CW is +], height from carpet) respectively.
 */
public class LiftMath {
    /**
     * Height (z position) (in) of bottom of lift reflexite from carpet
     */
    public static final double LIFT_LEFT_BOT_Z = 0; // TODO: set value

    /**
     * Height (z position) (in) of top of lift reflexite from carpet
     */
    public static final double LIFT_LEFT_TOP_Z = 0; // TODO: set value

    /**
     * Vertical length (in) of lift reflexite
     */
    public static final double LIFT_HEIGHT = 0; // TODO: set value

    /**
     * Width from the leftmost point on lift area to rightmost point.
     */
    public static final double INCH_LIFT_WIDTH = 0; // TODO: set value

    /**
     * Z position of lift camera.
     */
    public static final double CAMERA_Z = 0; // TODO: set value

    /**
     * Distance from the peg at which we want to meet the normal to the lift.
     * Should be as small as possible while still great enough for us to be
     * able to rotate ("pivot") to point at the
     * peg.
     */
    public static final double MIN_PIVOT_DIST = feet(4.0);

    ////////////////////////////////////////////////////////////////////////////
    // PART ONE: ///////////////////////////////////////////////////////////////
    // (1) We create first the triangle of [the leftmost point of lift], [rightmost
    // point on lift], and [camera].
    // (2) Then cut that in half for the triangle of [the leftmost point on
    // lift], [peg base (halfway between leftmost and rightmost)], and [camera].
    /**
     * Distance from camera to peg.
     */
    public double peg_r;

    /**
     * Note a convention here: variables are named [unit]_[description].
     * Units:
     * px: pixels in frame
     * deg: degrees
     * inch: inches in physical space
     *
     * @param deg_cam_from_lift_left Degrees from horizon to the bottom of the leftmost part of the lift area
     *
     * @param deg_cam_from_lift_right Degrees from horizon to the bottom of the rightmost part of the lift area
     *
     * @return Distance in inches from camera to peg base. Note: "peg base" is the point where the line on which
     * the peg lies intersects the surface of the wall on which the reflexite lies. So, a bit behind the peg.
     * (D_3 in drawing).
     */
    public double get_inch_pegDistance(double deg_cam_from_lift_left, double deg_cam_from_lift_right) {
        /**
         * Z-distance from bottom of lift target to the camera (in).
         * h in drawing.
         */
        double inch_rel_cam_height = CAMERA_Z - LIFT_LEFT_BOT_Z;

        /**
         * Distance to the leftmost (bottom) point on the lift area.
         * D_1 in drawing.
         */
        double inch_distance_to_lift_leftmost = inch_rel_cam_height * cot(frame_px_y_to_angle(deg_cam_from_lift_left, 0.0));

        /**
         * Distance to the rightmost (bottom) point on the lift area.
         * D_2 in drawing.
         */
        double inch_distance_to_lift_rightmost = inch_rel_cam_height * cot(frame_px_y_to_angle(deg_cam_from_lift_right, 0.0));

        /**
         * Distance to the peg base.
         */
        double inch_distance_to_peg = avg(inch_distance_to_lift_leftmost, inch_distance_to_lift_rightmost);

        this.peg_r = inch_distance_to_peg;
        return inch_distance_to_peg;
    }

    /**
     * @return gamma (see diagram).
     */
    public double get_deg_gamma(double deg_lift_area_width) {
        double gamma = arcsin(this.peg_r * Math.sin(deg_lift_area_width / 2) * 2 / INCH_LIFT_WIDTH);
        return gamma;
    }

    public double deg_delta;
    public double inch_d_t;
    public double deg_second_rotation;


    // PART TWO:


    ////////////////////////////////////////////////////////////////////////////
    // UTIL: ///////////////////////////////////////////////////////////////////
    private static double feet(double in) {
        return in * 12.0;
    }

    private static double cot(double theta) {
        return 1 / Math.tan(theta);
    }

    private static double arcsin(double theta) {
        return Math.asin(theta);
    }

    private static double avg(double ...vals) {
        double sum = 0;
        for (double val: vals) {
            sum += val;
        }
        return sum / vals.length;
    }

    private static double frame_px_x_to_angle(double frame_x0, double frame_x1) {
        // TODO: Write pinhole camera math (or linear approximation first).
        return 0;
    }

    private static double frame_px_y_to_angle(double frame_y0, double frame_y1) {
        // TODO: Write pinhole camera math (or linear approximation first).
        return 0;
    }
}
