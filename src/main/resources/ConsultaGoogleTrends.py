# import the TrendReq method from the pytrends request module
from pytrends.request import TrendReq
import sys

# execute the TrendReq method by passing the host language (hl) and timezone (tz) parameters
pytrends = TrendReq(hl='en-US', tz=360)

# build list of keywords
kw_list = ["ai", "chicken", "space"]

# build the payload
#pytrends.build_payload(kw_list, timeframe=sys.argv[1] + ' ' + sys.argv[2], geo='BR')
# periodo = sys.argv[1] + ' 2021-07-31'
# print(periodo)
pytrends.build_payload([sys.argv[3]], timeframe=sys.argv[1] + ' ' + sys.argv[2], geo='BR')

# store interest over time information in df
df = pytrends.interest_over_time()

# display the top 20 rows in dataframe
print(df.head(31))