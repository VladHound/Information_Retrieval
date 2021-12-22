import csv
import json

csv_file = open('kinoafisha_data.csv', 'r', encoding='cp1251')
json_file = open('kinoafisha_data.json', 'w', encoding='utf-8')

with open('kinoafisha_data.csv') as f:
    size = len(f.readlines())

fieldnames = ('position', 'title', 'genres', 'year', 'countries', 'rate', 'link')
reader = csv.DictReader(csv_file, fieldnames)

i = 1
json_file.write('[\n')
for row in reader:
    json.dump(row, json_file, indent=4, ensure_ascii=False)
    if i != size:
        json_file.write(',\n')
    i += 1
json_file.write('\n]')
