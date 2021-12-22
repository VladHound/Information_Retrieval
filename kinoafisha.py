import requests
from bs4 import BeautifulSoup
import pandas as pd

data = []

for page in range(0, 10):
    print('Страница', page + 1)
    url = f'https://www.kinoafisha.info/rating/movies/?page={page}'
    response = requests.get(url)
    soup = BeautifulSoup(response.text, 'lxml')
    films = soup.findAll('div', class_='movieList_item')

    for film in films:
        link = film.find('a', class_='movieItem_ref').get('href')
        position = film.find('div', class_='movieItem_info').find('span', class_='movieItem_position').text
        title = film.find('div', class_='movieItem_info').find('span', class_='movieItem_title').text
        genres = film.find('div', class_='movieItem_info').find('span', class_='movieItem_genres').text
        genres_list = genres.split(', ')
        len_genres = len(genres_list)
        sep_genres = genres_list[0]
        for g in range(1, len_genres):
            genre = genres_list[g]
            sep_genres += ' ' + genre
            g += 1
        year_countries = film.find('div', class_='movieItem_info').find('span', class_='movieItem_year').text
        year = year_countries.split(', ')[0]
        countries = year_countries.split(', ')[1]
        countries_list = countries.split(' / ')
        len_countries = len(countries_list)
        sep_countries = countries_list[0]
        for c in range(1, len_countries):
            country = countries_list[c]
            sep_countries += ' ' + country
            c += 1
        rate = film.find('div', class_='movieItem_info').find('span', class_='rating_num').text

        header = [position, title, sep_genres, year, sep_countries, rate, link]
        data.append(header)
        df = pd.DataFrame(data, columns=header)
        df.to_csv('kinoafisha_data.csv', header=False, index=False, encoding='cp1251')

        # print(position + ',' + title + ',' + sep_genres + ',' + year + ',' + sep_countries + ',' + rate + ',' + link)
