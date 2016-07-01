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

#### search

**GET** '/'

query params

- **f_doujinshi** `[0|1]`

- **f_manga** `[0|1]`

- **f_artistcg** `[0|1]`

- **f_gamecg** `[0|1]`

- **f_western** `[0|1]`

- **f_non-h** `[0|1]`

- **f_imageset** `[0|1]`

- **f_cosplay** `[0|1]`

- **f_asianporn** `[0|1]`

- **f_misc** `[0|1]`

- **f_search** `{search content}`

- **f_apply** `Apply+Filter`

#### register

##### step 1

**GET** '/index.php'

query params

- **act** `Reg`

- **CODE** `00`

access register page form
`div.page > div > form`

##### step 2

**POST** register page form from `step1`

data-form

- **agree_to_terms** `1`

access register page

##### step 3

**POST** '/index.php'

data-form

- **temporary_https** `1`

- **act** `Reg`

- **termsread** `1`

- **agree_to_terms** `1`

- **CODE** `02`

- **coppa_user** `0`

- **UserName** `{user_name}`

- **members_display_name** `{display_name}`

- **PassWord** `{password}`

- **PassWord_Check** `{password_again}`

- **EmailAddress** `{email}`

- **EmailAddress_two** `{email_again}`

- **time_offset** `{time_zone}` [0-23 default: 8]

- **regid** `{reg_code_id}`

- **reg_code** `{reg_code}`

access {reg_code} and {reg_code_id}
`div.tablepad > table > tbody > tr:nth-child(2) > td > fieldset.row3 > table > tbody > tr > td > img`
