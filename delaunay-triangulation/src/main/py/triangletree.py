from typing import Sequence

import pygame

from src.main.py.point import Point
from src.main.py.triangle import Triangle


# Add some space around the extremes of the point cloud to ensure
# that all points really fit inside the root triangle and don't fall on its edges.
ROOT_PADDING = 10


class TriangleTreeNode(Triangle):

    def __init__(self, point_a: Point, point_b: Point, point_c: Point,
                 parent: Triangle | None) -> None:
        super().__init__(point_a, point_b, point_c)
        self.parent = parent
        self.child_a: TriangleTreeNode | None = None
        self.child_b: TriangleTreeNode | None = None
        self.child_c: TriangleTreeNode | None = None

    def insert(self, point: Point) -> bool:
        if self.contains_point(point):
            if self.child_a is None:
                self.child_a = TriangleTreeNode(self.vertex_a, self.vertex_b, point, self)
                self.child_b = TriangleTreeNode(self.vertex_b, self.vertex_c, point, self)
                self.child_c = TriangleTreeNode(self.vertex_c, self.vertex_a, point, self)
                return True
            return self.child_a.insert(point) or self.child_b.insert(point) or self.child_c.insert(point)
        return False

    def get_leaf_nodes(self, leaves: list[Triangle]) -> None:
        if self.child_a is None:
            # if self.vertex_a.is_root_vertex or self.vertex_b.is_root_vertex or self.vertex_c.is_root_vertex:
            #     return
            leaves.append(self)
        else:
            self.child_a.get_leaf_nodes(leaves)
            self.child_b.get_leaf_nodes(leaves)
            self.child_c.get_leaf_nodes(leaves)


def get_triangles(points: Sequence[Point]) -> list[Triangle]:
    if not points:
        return []
    root = construct_root(points)
    for p in points:
        root.insert(p)

    # FIXME: Currently, retrieving the leaf nodes without the ones touching the root node results in disconnected points.
    # I think I have to first flip the edges of neighboring triangles.
    # See https://github.com/jdiemke/delaunay-triangulator/blob/master/library/src/main/java/io/github/jdiemke/triangulation/DelaunayTriangulator.java#L142
    leaves: list[Triangle] = []
    root.get_leaf_nodes(leaves)
    return leaves


def construct_root(points: Sequence[pygame.Vector2]) -> TriangleTreeNode:
    """Construct a triangle that contains all points."""
    x_min = min(points, key=lambda p: p.x).x - ROOT_PADDING
    x_max = max(points, key=lambda p: p.x).x + ROOT_PADDING
    y_min = min(points, key=lambda p: p.y).y - ROOT_PADDING
    y_max = max(points, key=lambda p: p.y).y + ROOT_PADDING
    vertex_a = Point(x_min, y_min, True)
    vertex_b = Point(x_max + x_max - x_min, y_min, True)
    vertex_c = Point(x_min, y_max + y_max - y_min, True)
    return TriangleTreeNode(vertex_a, vertex_b, vertex_c, None)
