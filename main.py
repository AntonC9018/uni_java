y = [113, 689, 379, 350, 346, 697, 355, 355]
z = []
for yi in y:
    z.append((yi * 28) % 523)


a = [3, 7, 13, 26, 65, 119, 267]

def decrypt(num):
    bits = []
    for i in range(len(a) - 1, -1, -1):
        if (num - a[i] >= 0):
            num -= a[i]
            bits.append(1)
        else:
            bits.append(0)
    return bits

x = []

for zi in z:
    res = decrypt(zi)
    for b in res:
        x.append(b)

m = []

for i in range(0, len(x), 8):
    ch = 0
    for j in range(0, 8):
        ch += x[i + j] << (j)
    m.append(chr(ch))

print("".join(m))