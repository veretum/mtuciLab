package com.mtuci;

/**
 * This class stores the basic state necessary for the A* algorithm to compute a
 * path across a map.  This state includes a collection of "open waypoints" and
 * another collection of "closed waypoints."  In addition, this class provides
 * the basic operations that the A* pathfinding algorithm needs to perform its
 * processing.
 **/

import java.util.*;

public class AStarState
{
    /** This is a reference to the map that the A* algorithm is navigating. **/
    private Map2D map;

    private Map<Location, Waypoint> open_waypoints, closed_waypoints;
    {
        open_waypoints = new HashMap<Location, Waypoint>();
        closed_waypoints = new HashMap<Location, Waypoint>();
    }


    /**
     * Initialize a new state object for the A* pathfinding algorithm to use.
     **/
    public AStarState(Map2D map)
    {
        if (map == null)
            throw new NullPointerException("map cannot be null");

        this.map = map;
    }

    /** Returns the map that the A* pathfinder is navigating. **/
    public Map2D getMap()
    {
        return map;
    }

    /**
     * This method scans through all open waypoints, and returns the waypoint
     * with the minimum total cost.  If there are no open waypoints, this method
     * returns <code>null</code>.
     **/
    public Waypoint getMinOpenWaypoint()
    {
        //если нет открытых точек, возвращает null
        if(numOpenWaypoints() == 0) return null;

        /**
        * Инициализация набора ключей всех открытых точек,
        * итератора для итерации по набору и переменную для хранения
        * наилучшей точки пути и стоимость этой точки.
         **/
        Set open_waypoint_keys = open_waypoints.keySet();
        Iterator i = open_waypoint_keys.iterator();
        Waypoint best = null;
        float best_cost = Float.MAX_VALUE;

        // Определение всех открытых путевых точек.
        while (i.hasNext())
        {
            // Текущее местоположение.
            Location location = (Location)i.next();
            // Хранить текущую путевую точку.
            Waypoint waypoint = open_waypoints.get(location);
            // Общая стоимость для текущей путевой точки.
            float waypoint_total_cost = waypoint.getTotalCost();

            /**
             * Сравнение наименьшей стоимости текущей точки
             * с наименьшей общей стоимостью сохраненной лучшей путевой точкой.
            **/
            if (waypoint_total_cost < best_cost)
            {
                best = open_waypoints.get(location);
                best_cost = waypoint_total_cost;
            }
        }
        // Возвращает точку с наименьшой общей стоимостью.
        return best;
    }

    /**
     * This method adds a waypoint to (or potentially updates a waypoint already
     * in) the "open waypoints" collection.  If there is not already an open
     * waypoint at the new waypoint's location then the new waypoint is simply
     * added to the collection.  However, if there is already a waypoint at the
     * new waypoint's location, the new waypoint replaces the old one <em>only
     * if</em> the new waypoint's "previous cost" value is less than the current
     * waypoint's "previous cost" value.
     **/
    public boolean addOpenWaypoint(Waypoint newWP)
    {
        // Нахождение местоположения новой путевой точки.
        Location location = newWP.getLocation();

        // Проверяет открыта ли точка на новом местоположении.
        if (open_waypoints.containsKey(location))
        {
            /**
             * Если у новой путевой точки открытое местоположение,
             * проверяет является ли "previous cost" предыдущей путевой точки
             * меньше значения "previous cost" текущей путевой точки
             **/
            Waypoint current_waypoint = open_waypoints.get(location);
            if (newWP.getPreviousCost() < current_waypoint.getPreviousCost())
            {
                /**
                 * Если значение "previous cost" новой точки меньше
                 * значения "previous cost" предыдущей путевой точки
                 * возвращает true
                 **/
                open_waypoints.put(location, newWP);
                return true;
            }
            /**
             * Если значение "previous cost" новой точки не меньше
             * значения "previous cost" предыдущей путевой точки
             * возвращает false
             */
            return false;
        }
        /**
         * Если в местоположении новой путевой точки нет открытой точки,
         * добавить новую путевую точку в коллекцию открытых точек
         * и вернуть true
         */
        open_waypoints.put(location, newWP);
        return true;
    }


    /** Returns the current number of open waypoints. **/
    public int numOpenWaypoints()
    {
        return open_waypoints.size();
    }


    /**
     * This method moves the waypoint at the specified location from the
     * open list to the closed list.
     **/
    public void closeWaypoint(Location loc)
    {
        Waypoint waypoint = open_waypoints.remove(loc);
        closed_waypoints.put(loc, waypoint);
    }

    /**
     * Returns true if the collection of closed waypoints contains a waypoint
     * for the specified location.
     **/
    public boolean isLocationClosed(Location loc)
    {
        return closed_waypoints.containsKey(loc);
    }
}
