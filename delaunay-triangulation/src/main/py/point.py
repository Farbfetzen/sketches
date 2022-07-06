import pygame


class Point(pygame.Vector2):

    def __init__(self, x: float, y: float, is_root_vertex: bool = False):
        super().__init__(x, y)
        self.is_root_vertex = is_root_vertex

    def draw(self, target: pygame.surface.Surface, color: pygame.Color, radius: float) -> None:
        pygame.draw.circle(target, color, self, radius)
