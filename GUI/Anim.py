import matplotlib
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation


def cleanMode():
    matplotlib.rcParams['toolbar'] = 'None'
    matplotlib.rcParams['figure.subplot.left'] = 0
    matplotlib.rcParams['figure.subplot.right'] = 1
    matplotlib.rcParams['figure.subplot.top'] = 1
    matplotlib.rcParams['figure.subplot.bottom'] = 0


def graphMode():
    matplotlib.rcParams['toolbar'] = 'toolbar2'
    matplotlib.rcParams['figure.subplot.bottom'] = 0.11
    matplotlib.rcParams['figure.subplot.left'] = 0.125
    matplotlib.rcParams['figure.subplot.right'] = 0.9
    matplotlib.rcParams['figure.subplot.top'] = 0.88


def loadMap(name):
    Szpitale = []
    Obiekty = []
    Skrzyzowania = []
    Drogi = []
    file = open(name, 'r')
    lines = file.read().split('\n')
    poly1 = [i.split() for i in lines[0].split('|')]
    polyx = [int(i[0]) for i in poly1]
    polyy = [int(i[1]) for i in poly1]
    for line in lines:
        words = line.split('|')
        if line[0] == 'S':
            Szpitale.append([words[1], float(words[2]), float(words[3])])
        if line[0] == 'K':
            Skrzyzowania.append([float(words[1]), float(words[2])])
        if line[0] == 'O':
            Obiekty.append([words[1], float(words[2]), float(words[3])])
        if line[0] == 'D':
            Drogi.append([int(words[1]), int(words[2])])
    return [Szpitale, Obiekty, Skrzyzowania, Drogi, polyx, polyy]


def plotSzpitale(Szpitale):
    x = [i[1] for i in Szpitale]
    y = [i[2] for i in Szpitale]
    plt.scatter(x, y, c='black', marker='P', zorder=20, s=45)


def plotObiekty(Obiekty):
    x = [i[1] for i in Obiekty]
    y = [i[2] for i in Obiekty]
    plt.scatter(x, y, c='black', marker='*', zorder=20, s=50)


def plotSkrzyzowania(Skrzyzowania):
    x = [i[0] for i in Skrzyzowania]
    y = [i[1] for i in Skrzyzowania]
    plt.scatter(x, y, c='black', marker='o', zorder=20, s=30)


def plotDrogi(Drogi, Szpitale):
    for droga in Drogi:
        plt.plot([Szpitale[droga[0]][1], Szpitale[droga[1]][1]],
                 [Szpitale[droga[0]][2], Szpitale[droga[1]][2]], zorder=10)


def decodeLocations(locations, Szpitale, Skrzyzowania):
    coordinates = []
    if locations == []:
        return 1
    for location in locations:
        if location[0] == 'S':
            coordinates.append([Szpitale[int(location[1:])][1],
                                Szpitale[int(location[1:])][2]])
        elif location[0] == 'K':
            coordinates.append([Skrzyzowania[int(location[1:])][0],
                                Skrzyzowania[int(location[1:])][1]])
    return coordinates


def loadPassengers(name):
    file = open(name, 'r')
    lines = file.read().split('\n')
    passengers = []
    for line in lines:
        cut = line.split('|')
        Id, x, y = cut[0].split()
        locations = cut[1].split()
        passengers.append([int(x), int(y), locations])
    return passengers


def createMove(x1, y1, x2, y2, frames):
    track = []
    xUp = (x2 - x1) / frames
    yUp = (y2 - y1) / frames
    for i in range(frames):
        track.append([x1 + xUp * i, y1 + yUp * i])
    return track


def trackAll(positions, frames):
    allPositions = []
    l = len(positions)
    first = 1
    if len(positions[0]) == 3:
        step = 7.5 / (frames * 3)
        allPositions.append([[positions[0][0], positions[0][1]], 'blue', 7.5 - step, 'origin'])
        for i in range(frames * 3 - 1):
            allPositions.append([[positions[0][0], positions[0][1]], 'blue', 7.5 - (i + 1) * step, 'duration'])
    else:
        for i in range(l - 1):
            for position in createMove(positions[i][0], positions[i][1],
                                       positions[i + 1][0], positions[i + 1][1], frames):
                if first == 1:
                    allPositions.append([position, 'red', 7.5, 'origin'])
                    first = 0
                allPositions.append([position, 'red', 7.5, 'duration'])
    return allPositions


def Main(Szpitale, Obiekty, Skrzyzowania, Drogi, in2, frames):
    passengers = loadPassengers(in2)
    positions = []
    allPositions = []
    for passenger in passengers:
        locations = decodeLocations(passenger[2], Szpitale, Skrzyzowania)
        if locations == 1:
            positions.append([passenger[0], passenger[1], 'out_of_bounds'])
        else:
            positions.append([passenger[0], passenger[1]])
            for location in locations:
                positions.append([location[0], location[1]])
        for position in trackAll(positions, frames):
            allPositions.append(position)
        positions = []
    return allPositions


def run(i):
    global x
    global leGen
    if (i - x) % (leGen - 1) == 0 and (i - x) != 0:
        x += leGen
    if not pause:
        point.set_data(iksde[i - x][0][0], iksde[i - x][0][1])
        point.set_color(iksde[i - x][1])
        point.set_markersize(iksde[i - x][2])
        if iksde[i - x][3] == 'origin':
            origin.set_data(iksde[i - x][0][0], iksde[i - x][0][1])
        return point
    else:
        x += 1


def onClick(event):
    global pause
    pause ^= True


def func(in1, in2, frames, cl):
    if cl == 1:
        cleanMode()
    else:
        graphMode()
    Szpitale, Obiekty, Skrzyzowania, Drogi, polyx, polyy = loadMap(in1)
    max_x, min_x, max_y, min_y = max(polyx), min(polyx), max(polyy), min(polyy)
    diff_x, diff_y = (max_x - min_x) * 0.1, (max_y - min_y) * 0.1
    fig, ax = plt.subplots(num='Animation', figsize=(5, 5), dpi=90, facecolor='w', edgecolor='k')
    ax.set_ylim(min_y - diff_y, max_y + diff_y)
    ax.set_xlim(min_x - diff_x, max_x + diff_x)
    global point, origin
    point, = ax.plot([0], [0], 'ro', zorder=30)
    origin, = ax.plot([0], [0], 'go', zorder=25)
    if cl == 0:
        ax.grid()
    fig.canvas.mpl_connect('button_press_event', onClick)
    plt.fill([min_x - diff_x, max_x + diff_x, max_x + diff_x, min_x - diff_x]
             , [min_y - diff_y, min_y - diff_y, max_y + diff_y, max_y + diff_y]
             , color='lightgray', zorder=-3)
    plt.fill(polyx, polyy, color='white', zorder=-2)
    plotSzpitale(Szpitale)
    plotObiekty(Obiekty)
    plotSkrzyzowania(Skrzyzowania)
    plotDrogi(Drogi, Szpitale)
    global iksde, x, leGen, pause
    iksde = Main(Szpitale, Obiekty, Skrzyzowania, Drogi, in2, frames)
    x = 0
    leGen = len(iksde)
    pause = False
    ani = FuncAnimation(fig, run, interval=10)
    plt.show()
