import pygame


COLOR = pygame.Color(0, 255, 0)
RADIUS = 4


class Point(pygame.Vector2):

    def __init__(self, x: float, y: float, is_root_vertex: bool = False):
        super().__init__(x, y)
        self.is_root_vertex = is_root_vertex

    def draw(self, target: pygame.surface.Surface) -> None:
        pygame.draw.circle(target, COLOR, self, RADIUS)
