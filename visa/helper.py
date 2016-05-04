import json

def row_jsonify(result, one=False):
    if result is None:
        return "Error, not found!"
    elif(one is True):
        t = dict(result)
        t["success"] = 1
        return json.dumps(t)
    else:
        return json.dumps([dict(x) for x in result])
