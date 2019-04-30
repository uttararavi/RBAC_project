import matplotlib.pyplot as plt

f = open('plt', 'r')

ax = plt.subplot(1, 1, 1)
numlines = 10
intervals = []
regions = []
for line in f:
	numlines += 1
	if numlines == 0:
		break
	if 'int' in line:
		intervals.append([(float(l.split(',')[0][1:]), float(l.split(',')[1][:-1])) for l in line[6:].split()])
		# print(intervals)
		# print()
	if 'region' in line:
		regions.append([int(i) for i in line[9:].split()])
		# print(regions)
		# print()
for i in range(len(intervals)):
	for j in range(len(intervals[i])):
		for k in range(len(regions[i])):
			print(intervals[i][j], [regions[i][k]]*2)
			plt.plot(intervals[i][j], [regions[i][k]]*2)
plt.show()