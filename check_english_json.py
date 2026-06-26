import json
import glob
import os

episodes = glob.glob("app/src/main/assets/english_episodes/*.json")
errors = []

for ep in episodes:
    try:
        with open(ep, 'r', encoding='utf-8') as f:
            data = json.load(f)
            
            # Check if it is a list (valid JSONArray format)
            if isinstance(data, list):
                if len(data) == 0:
                    errors.append(f"{ep}: Empty JSON array")
                for i, ex in enumerate(data):
                    if not isinstance(ex, dict):
                        errors.append(f"{ep}: Node {i} is not a JSON object")
                        continue
                    if "type" not in ex:
                        errors.append(f"{ep}: Node {i} missing 'type'")
            
            # Check if it is a dict (valid JSONObject format)
            elif isinstance(data, dict):
                # We expect "nodes" or "exercises"
                nodes = data.get("nodes") or data.get("exercises")
                if nodes is None:
                    errors.append(f"{ep}: Missing 'nodes' or 'exercises' key in JSON object")
                elif not isinstance(nodes, list):
                    errors.append(f"{ep}: 'nodes' or 'exercises' must be a JSON array")
                else:
                    for i, ex in enumerate(nodes):
                        if not isinstance(ex, dict):
                            errors.append(f"{ep}: Exercise {i} is not a JSON object")
                            continue
                        if "type" not in ex:
                            errors.append(f"{ep}: Exercise {i} missing 'type'")
            else:
                errors.append(f"{ep}: Invalid JSON root type (must be list or dict)")
                
    except json.JSONDecodeError as e:
        errors.append(f"{ep}: JSON parse error: {e}")
    except Exception as e:
        errors.append(f"{ep}: General error: {e}")

if errors:
    print(f"Found {len(errors)} errors:")
    for err in errors[:50]:
        print(err)
else:
    print(f"Checked {len(episodes)} episodes, all structured correctly as valid lists or objects!")
