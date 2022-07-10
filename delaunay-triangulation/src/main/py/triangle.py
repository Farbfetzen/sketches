import pygame

from src.main.py.point import Point


COLOR = pygame.Color(255, 128, 0)
LINE_WIDTH = 1


class Triangle:

    def __init__(self, point_a: Point, point_b: Point, point_c: Point) -> None:
        self.vertex_a = point_a
        self.vertex_b = point_b
        self.vertex_c = point_c

    def contains_point(self, point: pygame.Vector2) -> bool:
        # Pygame provides the wrong type annotation for the return type of pygame.Vector2.cross.
        # Make mypy ignore these lines until that is fixed.
        # https://github.com/pygame/pygame/issues/3282
        abp: float = (self.vertex_b - self.vertex_a).cross(point - self.vertex_a)  # type: ignore
        bcp: float = (self.vertex_c - self.vertex_b).cross(point - self.vertex_b)  # type: ignore
        if not self.same_sign(abp, bcp):
            return False
        acp: float = (self.vertex_a - self.vertex_c).cross(point - self.vertex_c)  # type: ignore
        return self.same_sign(abp, acp)

    @staticmethod
    def same_sign(a: float, b: float) -> bool:
        return (a > 0) == (b > 0)

    def draw(self, target: pygame.surface.Surface) -> None:
        pygame.draw.polygon(target, COLOR, (self.vertex_a, self.vertex_b, self.vertex_c), LINE_WIDTH)
