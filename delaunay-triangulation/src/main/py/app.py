import random

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
POINT_COLOR = pygame.Color(0, 255, 0)
POINT_RADIUS = 4


def generate_points() -> list[Point]:
    return [Point(random.uniform(MARGIN, WINDOW_WIDTH - MARGIN), random.uniform(MARGIN, WINDOW_HEIGHT - MARGIN))
            for _ in range(N_POINTS)]


def run() -> None:
    window = pygame.display.set_mode((WINDOW_WIDTH, WINDOW_HEIGHT))
    clock = pygame.time.Clock()
    points = generate_points()
    triangles = triangletree.get_triangles(points)

    while True:
        clock.tick(MAX_FPS)
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                return
            elif event.type == pygame.KEYDOWN:
                if event.key == pygame.K_ESCAPE:
                    return
                elif event.key == pygame.K_r:
                    points = generate_points()
                    triangles = triangletree.get_triangles(points)

        window.fill(BACKGROUND_COLOR)
        for p in points:
            p.draw(window, POINT_COLOR, POINT_RADIUS)
        for t in triangles:
            t.draw(window)
        pygame.display.flip()
