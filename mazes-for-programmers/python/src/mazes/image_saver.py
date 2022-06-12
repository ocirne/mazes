from datetime import datetime


def save(img, filename=None, lfd=0, extension="png"):
    if filename is None:
        filename = datetime.now().strftime("%%Y-%%m-%%d-%%H%%M%%S-%s.%s" % (lfd, extension))
    print("write to file", filename)
    img.save("images/" + filename)
