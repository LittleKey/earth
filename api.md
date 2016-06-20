e-hentai api
============

host `https://forums.e-hentai.org`

## login

**POST** `/index.php?act=Login&CODE=01`

#### Body

form-data

- **CookieDate** `1`

- **UserName** `{username}`

- **PassWord** `{password}`

## exhentai

host `http://exhentai.org`

#### Header

`user_id` and `user_pass_hash` get from login response `headers.get('Set-Cookie')`

```
user_id = login_response_header['Set-Cookie'].findall("ipb_member_id=(.*?)")
user_pass_hash = login_response_header['Set-Cookie'].findall("ipb_pass_hash=(.*?)")
```

- **Cookie** `ipb_member_id={{ user_id }};ipb_pass_hash={{ user_pass_hash }}`

#### add to favorites

**POST** `/gallerypopups.php`

query params

- **gid** `{grallery_id}`

- **t** `{grallery_token}`

- **act** `addfav`

from data

- **favcat** `{fav_id}` [0~9 and favdel]

- **favnote** `{note}`(optional)

- **apply** `Add to Favorites` or `Apply Changes`

- **update** `1`
