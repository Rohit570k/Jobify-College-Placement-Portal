const notFoundMiddleware = (req,res) =>
    res.status(404).send('<h1>Route doesnt exists</h1>')

export default notFoundMiddleware;