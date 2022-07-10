import pygame

from src.main.py import triangletree
from src.main.py.point import Point


pygame.init()

WINDOW_WIDTH = 1200
WINDOW_HEIGHT = 800
BACKGROUND_COLOR = pygame.Color(32, 32, 32)
MAX_FPS = 60
N_POINTS = 10
MARGIN = 100
POINT_DELETE_DISTANCE = 5


class App:

    def __init__(self) -> None:
        self.canvas = pygame.display.set_mode((WINDOW_WIDTH, WINDOW_HEIGHT))
        self.clock = pygame.time.Clock()
        self.points: list[Point] = []
        self.triangles = triangletree.get_triangles(self.points)
        self.is_running = True

    def handle_input(self) -> None:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                self.is_running = False
            elif event.type == pygame.KEYDOWN:
                if event.key == pygame.K_ESCAPE:
                    self.is_running = False
                elif event.key == pygame.K_DELETE:
                    self.points = []
                    self.triangles = triangletree.get_triangles(self.points)
            elif event.type == pygame.MOUSEBUTTONDOWN:
                if event.button == 1:
                    self.points.append(Point(*event.pos))
                elif event.button == 3:
                    self.points = [p for p in self.points if p.distance_to(event.pos) > POINT_DELETE_DISTANCE]
                self.triangles = triangletree.get_triangles(self.points)

    def draw(self) -> None:
        self.canvas.fill(BACKGROUND_COLOR)
        for t in self.triangles:
            t.draw(self.canvas)
        for p in self.points:
            p.draw(self.canvas)
        pygame.display.flip()

    def run(self) -> None:
        while self.is_running:
            self.clock.tick(MAX_FPS)
            self.handle_input()
            self.draw()
