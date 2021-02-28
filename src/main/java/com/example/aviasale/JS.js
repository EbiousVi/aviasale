const a = Date.parse("2017-09-09 18:15:00");
const b = Date.parse("2017-09-09 20:30:00");
const diff = b - a;
const h = (diff / 3600000).toFixed(0);
const m = Math.floor((diff / (60 * 1000)) % 60);
console.log(h.concat("h ").concat(m.toString()).concat("m"))
